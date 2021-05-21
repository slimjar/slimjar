package io.github.slimjar.downloader;

import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.resolver.DependencyResolver;

public final class URLDependencyDownloaderFactory implements DependencyDownloaderFactory {

    @Override
    public DependencyDownloader create(final OutputWriterFactory outputWriterFactory, final DependencyResolver resolver) {
        return new URLDependencyDownloader(outputWriterFactory, resolver);
    }
}
