package io.github.slimjar.downloader;

import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.downloader.verify.DependencyVerifier;
import io.github.slimjar.resolver.DependencyResolver;

public final class URLDependencyDownloaderFactory implements DependencyDownloaderFactory {

    @Override
    public DependencyDownloader create(final OutputWriterFactory outputWriterFactory, final DependencyResolver resolver, final DependencyVerifier verifier) {
        return new URLDependencyDownloader(outputWriterFactory, resolver, verifier);
    }
}
