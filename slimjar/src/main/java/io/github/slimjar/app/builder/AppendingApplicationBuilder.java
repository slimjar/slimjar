package io.github.slimjar.app.builder;

import io.github.slimjar.app.AppendingApplication;
import io.github.slimjar.app.Application;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.injector.loader.Injectable;
import io.github.slimjar.injector.loader.WrappedInjectableClassLoader;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.DependencyDataProvider;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.security.NoSuchAlgorithmException;

public final class AppendingApplicationBuilder extends ApplicationBuilder {
    private final URLClassLoader classLoader;

    public AppendingApplicationBuilder(final String applicationName, final URLClassLoader classLoader) {
        super(applicationName);
        this.classLoader = classLoader;
    }

    @Override
    public Application build() throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException {
        final DependencyDataProvider dataProvider = getDataProviderFactory().create(getDependencyFileUrl());
        final DependencyData dependencyData = dataProvider.get();
        final DependencyInjector dependencyInjector = createInjector();
        final Injectable injectable = new WrappedInjectableClassLoader(classLoader);
        dependencyInjector.inject(injectable, dependencyData);
        return new AppendingApplication();
    }
}
