package io.github.slimjar.resolver.reader;

import io.github.slimjar.resolver.reader.facade.GsonFacade;
import io.github.slimjar.resolver.reader.facade.GsonFacadeFactory;

import java.net.URL;

public final class GsonDependencyDataProviderFactory implements DependencyDataProviderFactory {
    private final GsonFacade gson;

    public GsonDependencyDataProviderFactory(final GsonFacadeFactory gsonFactory) throws ReflectiveOperationException {
        this.gson = gsonFactory.createFacade();
    }

    public GsonDependencyDataProviderFactory(final GsonFacade gson) {
        this.gson = gson;
    }


    public DependencyDataProvider create(final URL dependencyFileURL) {
        final DependencyReader dependencyReader = new GsonDependencyReader(gson);
        return new URLDependencyDataProvider(dependencyReader, dependencyFileURL);
    }
}
