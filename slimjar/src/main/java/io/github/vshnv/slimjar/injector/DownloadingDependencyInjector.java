package io.github.vshnv.slimjar.injector;

import io.github.vshnv.slimjar.data.Dependency;
import io.github.vshnv.slimjar.data.DependencyData;
import io.github.vshnv.slimjar.downloader.DependencyDownloader;
import io.github.vshnv.slimjar.injector.loader.Injectable;

import java.io.IOException;
import java.net.URL;

public final class DownloadingDependencyInjector implements DependencyInjector {
    private final DependencyDownloader dependencyDownloader;

    public DownloadingDependencyInjector(DependencyDownloader dependencyDownloader) {
        this.dependencyDownloader = dependencyDownloader;
    }

    @Override
    public void inject(Injectable injectable, DependencyData dependencyData) {
        for (Dependency dependency : dependencyData.getDependencies()) {
            try {
                URL downloadedJarUrl = dependencyDownloader.download(dependency);
                injectable.inject(downloadedJarUrl);
            } catch (IOException e) {
                throw new InjectionFailedException(dependency, e);
            }
        }
    }
}
