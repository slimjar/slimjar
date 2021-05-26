package io.github.slimjar.downloader;

import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.downloader.verify.DependencyVerifier;
import io.github.slimjar.resolver.DependencyResolver;

@FunctionalInterface
public interface DependencyDownloaderFactory {
    DependencyDownloader create(final OutputWriterFactory outputWriterFactory, final DependencyResolver resolver, final DependencyVerifier verifier);
}
