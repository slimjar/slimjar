package io.github.slimjar.injector.loader;

import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.app.module.ModuleExtractor;
import io.github.slimjar.app.module.TemporaryModuleExtractor;
import io.github.slimjar.injector.agent.ByteBuddyInstrumentationFactory;
import io.github.slimjar.injector.agent.InstrumentationFactory;
import io.github.slimjar.injector.loader.manifest.JarManifestGenerator;
import io.github.slimjar.injector.loader.manifest.ManifestGenerator;
import io.github.slimjar.relocation.JarFileRelocator;
import io.github.slimjar.relocation.PassthroughRelocator;
import io.github.slimjar.relocation.RelocationRule;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.relocation.facade.JarRelocatorFacadeFactory;
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



    private final Instrumentation instrumentation;

    public InstrumentationInjectable(final Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void inject(final URL url) throws IOException {
        instrumentation.appendToSystemClassLoaderSearch(new JarFile(url.getPath()));
    }

    public static Injectable create() throws IOException, NoSuchAlgorithmException, ReflectiveOperationException, URISyntaxException {
        return create(new ByteBuddyInstrumentationFactory());
    }

    public static Injectable create(final InstrumentationFactory factory) throws IOException, NoSuchAlgorithmException, ReflectiveOperationException, URISyntaxException {
        return new InstrumentationInjectable(factory.create());
    }

    public static DependencyData getDependency() throws MalformedURLException {
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
