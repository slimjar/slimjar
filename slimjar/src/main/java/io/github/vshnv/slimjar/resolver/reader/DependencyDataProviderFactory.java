package io.github.vshnv.slimjar.resolver.reader;

import com.google.gson.Gson;

import java.net.URL;

public final class DependencyDataProviderFactory {
    private final Gson gson;

    public DependencyDataProviderFactory(final Gson gson) {
        this.gson = gson;
    }

    public DependencyDataProvider create(final URL dependencyFileURL) {
        final DependencyReader dependencyReader = new GsonDependencyReader(gson);
        return new FileDependencyDataProvider(dependencyReader, dependencyFileURL);
    }
}
