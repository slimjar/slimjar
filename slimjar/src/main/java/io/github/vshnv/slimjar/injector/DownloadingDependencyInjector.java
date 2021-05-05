package io.github.vshnv.slimjar.injector;

import io.github.vshnv.slimjar.downloader.DependencyDownloader;
import io.github.vshnv.slimjar.injector.loader.Injectable;
import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.DependencyData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collection;

public final class DownloadingDependencyInjector implements DependencyInjector {
    private final DependencyDownloader dependencyDownloader;

    public DownloadingDependencyInjector(final DependencyDownloader dependencyDownloader) {
        this.dependencyDownloader = dependencyDownloader;
    }

    @Override
    public void inject(final Injectable injectable, final Collection<Dependency> dependencies) {
        for (final Dependency dependency : dependencies) {
            try {
                final URL downloadedJarUrl = dependencyDownloader.download(dependency);
                injectable.inject(downloadedJarUrl);
            } catch (final IOException e) {
                throw new InjectionFailedException(dependency, e);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
