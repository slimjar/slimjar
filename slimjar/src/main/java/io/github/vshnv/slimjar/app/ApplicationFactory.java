package io.github.vshnv.slimjar.app;

import io.github.vshnv.slimjar.injector.DependencyInjector;
import io.github.vshnv.slimjar.injector.loader.Injectable;
import io.github.vshnv.slimjar.injector.loader.InjectableClassLoader;
import io.github.vshnv.slimjar.injector.loader.WrappedInjectableClassLoader;
import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.DependencyData;
import io.github.vshnv.slimjar.resolver.reader.DependencyDataProvider;
import io.github.vshnv.slimjar.util.Parameters;

import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

public final class ApplicationFactory {
    private final ApplicationConfiguration applicationConfiguration;
    private ApplicationFactory(final ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public Application createIsolatedApplication(final String fqClassName, final Object... args) throws ReflectiveOperationException, ClassCastException {
        final DependencyData dependencyData = applicationConfiguration.getDependencyDataProvider().get();
        final DependencyInjector dependencyInjector = applicationConfiguration.getDependencyInjector();
        final InjectableClassLoader classLoader = new InjectableClassLoader(fqClassName);
        dependencyInjector.inject(classLoader, dependencyData);
        final Class<Application> applicationClass = (Class<Application>) Class.forName(fqClassName, true, classLoader);
        return applicationClass.getConstructor(Parameters.typesFrom(args)).newInstance(args);
    }

    public Application createAppendingApplication(final URLClassLoader classLoader) {
        final DependencyData dependencyData = applicationConfiguration.getDependencyDataProvider().get();
        final DependencyInjector dependencyInjector = applicationConfiguration.getDependencyInjector();
        final Injectable injectable = new WrappedInjectableClassLoader(classLoader);
        dependencyInjector.inject(injectable, dependencyData);
        return new AppendingApplication();
    }

}
