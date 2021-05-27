package io.github.slimjar.injector.loader;

import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.app.module.ModuleExtractor;
import io.github.slimjar.app.module.TemporaryModuleExtractor;
import io.github.slimjar.relocation.JarFileRelocator;
import io.github.slimjar.relocation.PassthroughRelocator;
import io.github.slimjar.relocation.RelocationRule;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacade;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.util.Packages;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.UUID;
import java.util.jar.JarFile;

public final class InstrumentationInjectable implements Injectable {
    private static final String AGENT_JAR = "loader-agent.isolated-jar";
    private static final String AGENT_PACKAGE = "io#github#slimjar#injector#agent";
    private static final String AGENT_CLASS = "ClassLoaderAgent";
    private static final String BYTEBUDDY_AGENT_CLASS = "net#bytebuddy#agent#ByteBuddyAgent";



    private final Instrumentation instrumentation;

    public InstrumentationInjectable(final Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void inject(final URL url) throws IOException {
        instrumentation.appendToSystemClassLoaderSearch(new JarFile(url.getPath()));
    }

    public static Injectable create() throws IOException, NoSuchAlgorithmException, ReflectiveOperationException, URISyntaxException {
        final URL url = InstrumentationInjectable.class.getClassLoader().getResource(AGENT_JAR);
        final ModuleExtractor extractor = new TemporaryModuleExtractor();
        final URL extractedURL = extractor.extractModule(url, "loader-agent");

        final JarRelocatorFacadeFactory facadeFactory = ReflectiveJarRelocatorFacadeFactory.create();
        final String pattern = generatePattern();
        final String relocatedAgentClass = String.format("%s.%s", pattern, AGENT_CLASS);
        final RelocationRule relocationRule = new RelocationRule(Packages.fix(AGENT_PACKAGE), pattern, Collections.emptySet(), Collections.emptySet());
        final Relocator relocator = new JarFileRelocator(Collections.singleton(relocationRule), facadeFactory);
        final File inputFile = new File(extractedURL.getPath());
        final File relocatedFile = File.createTempFile("slimjar-agent", ".jar");
        relocator.relocate(inputFile, relocatedFile);

        final InjectableClassLoader classLoader = new IsolatedInjectableClassLoader();

        ApplicationBuilder.injecting("SlimJar-Agent", classLoader)
                .dataProviderFactory((dataUrl) -> InstrumentationInjectable::getDependency)
                .relocatorFactory((rules) -> new PassthroughRelocator())
                .relocationHelperFactory((rel) -> (dependency,file) -> file)
                .build();
        final Class<?> byteBuddyAgentClass = Class.forName(Packages.fix(BYTEBUDDY_AGENT_CLASS), true, classLoader);
        final Method attachMethod = byteBuddyAgentClass.getMethod("attach", File.class, String.class, String.class);

        final Class<?> processHandle = Class.forName("java.lang.ProcessHandle");
        final Method currentMethod = processHandle.getMethod("current");
        final Method pidMethod = processHandle.getMethod("pid");
        final Object currentProcess = currentMethod.invoke(processHandle);
        final Long processId = (Long) pidMethod.invoke(currentProcess);


        attachMethod.invoke(null, relocatedFile, String.valueOf(processId), "");

        final Class<?> agentClass = Class.forName(relocatedAgentClass, true, ClassLoader.getSystemClassLoader());
        final Method instrMethod = agentClass.getMethod("getInstrumentation");
        final Instrumentation instrumentation = (Instrumentation) instrMethod.invoke(null);
        return new InstrumentationInjectable(instrumentation);
    }

    private static DependencyData getDependency() throws MalformedURLException {
        final Dependency byteBuddy = new Dependency(
                "net.bytebuddy",
                "byte-buddy-agent",
                "1.11.0",
                null,
                Collections.emptyList()
        );
        final Repository centralRepository = new Repository(new URL(SimpleMirrorSelector.CENTRAL_URL));
        return new DependencyData(
                Collections.emptySet(),
                Collections.singleton(centralRepository),
                Collections.singleton(byteBuddy),
                Collections.emptyList()
        );
    }

    private static String generatePattern() {
        return String.format("slimjar.%s", UUID.randomUUID().toString());
    }
}
