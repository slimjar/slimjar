package io.github.vshnv.slimjar.app;

import com.google.gson.Gson;
import io.github.vshnv.slimjar.app.module.ModuleExtractor;
import io.github.vshnv.slimjar.app.module.TemporaryModuleExtractor;
import io.github.vshnv.slimjar.injector.DependencyInjector;
import io.github.vshnv.slimjar.injector.loader.Injectable;
import io.github.vshnv.slimjar.injector.loader.InjectableClassLoader;
import io.github.vshnv.slimjar.injector.loader.IsolatedInjectableClassLoader;
import io.github.vshnv.slimjar.injector.loader.WrappedInjectableClassLoader;
import io.github.vshnv.slimjar.resolver.data.DependencyData;
import io.github.vshnv.slimjar.resolver.reader.DependencyDataProvider;
import io.github.vshnv.slimjar.resolver.reader.DependencyDataProviderFactory;
import io.github.vshnv.slimjar.util.Parameters;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public final class ApplicationFactory {
    private final ApplicationConfiguration applicationConfiguration;

    public ApplicationFactory(final ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public Application createIsolatedApplication(final Collection<String> moduleNames, final String fqClassName, final Object... args) throws ReflectiveOperationException, ClassCastException, IOException {
        final Gson gson = new Gson();
        final ModuleExtractor moduleExtractor = new TemporaryModuleExtractor();
        final URL[] extractedModules = findExtractedModules(moduleExtractor, moduleNames);
        final DependencyDataProviderFactory dependencyDataProviderFactory = new DependencyDataProviderFactory(gson);
        final InjectableClassLoader classLoader = new IsolatedInjectableClassLoader(extractedModules, Collections.singleton(Application.class), Collections.emptyList());
        final DependencyInjector dependencyInjector = applicationConfiguration.getDependencyInjector();

        for (final URL module : extractedModules) {
            final DependencyDataProvider dataProvider = dependencyDataProviderFactory.forModule(module);
            final DependencyData dependencyData = dataProvider.get();
            dependencyInjector.inject(classLoader, dependencyData.getDependencies());
        }
        final Class<Application> applicationClass = (Class<Application>) Class.forName(fqClassName, true, classLoader);
        return applicationClass.getConstructor(Parameters.typesFrom(args)).newInstance(args);
    }

    public Application createIsolatedApplication(final String fqClassName, final Object... args) throws ReflectiveOperationException, ClassCastException {
        final DependencyData dependencyData = applicationConfiguration.getDependencyData();
        final DependencyInjector dependencyInjector = applicationConfiguration.getDependencyInjector();
        final InjectableClassLoader classLoader = new IsolatedInjectableClassLoader();
        dependencyInjector.inject(classLoader, dependencyData.getDependencies());
        final Class<Application> applicationClass = (Class<Application>) Class.forName(fqClassName, true, classLoader);
        return applicationClass.getConstructor(Parameters.typesFrom(args)).newInstance(args);
    }

    public Application createAppendingApplication(final URLClassLoader classLoader) {
        final DependencyData dependencyData = applicationConfiguration.getDependencyData();
        final DependencyInjector dependencyInjector = applicationConfiguration.getDependencyInjector();
        final Injectable injectable = new WrappedInjectableClassLoader(classLoader);
        dependencyInjector.inject(injectable, dependencyData.getDependencies());
        return new AppendingApplication();
    }

    private static URL findModule(final String moduleName) {
        final ClassLoader classLoader = ApplicationFactory.class.getClassLoader();
        return classLoader.getResource(moduleName + ".isolated-jar");
    }

    private static URL[] findExtractedModules(final ModuleExtractor extractor, final Collection<String> moduleNames) throws IOException {
        final Collection<URL> result = new HashSet<>();
        for (final String moduleName : moduleNames) {
            final URL modulePath = findModule(moduleName);
            final URL extractedModule = extractor.extractModule(modulePath, moduleName);
            result.add(extractedModule);
        }
        return result.stream().toArray(URL[]::new);
    }

}
