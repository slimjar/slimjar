package io.github.vshnv.slimjar.app;

import io.github.vshnv.slimjar.data.reader.DependencyDataProvider;
import io.github.vshnv.slimjar.downloader.path.FilePathStrategy;
import io.github.vshnv.slimjar.injector.DependencyInjector;

public final class ApplicationConfiguration {
    private final DependencyInjector dependencyInjector;
    private final DependencyDataProvider dependencyDataProvider;
    private final FilePathStrategy filePathStrategy;

    public ApplicationConfiguration(DependencyInjector dependencyInjector, DependencyDataProvider dependencyDataProvider, FilePathStrategy filePathStrategy) {
        this.dependencyInjector = dependencyInjector;
        this.dependencyDataProvider = dependencyDataProvider;
        this.filePathStrategy = filePathStrategy;
    }
}
