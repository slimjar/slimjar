package io.github.slimjar.downloader.verify;

import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.resolver.DependencyResolver;

public class ChecksumDependencyVerifierFactory implements DependencyVerifierFactory {
    private final OutputWriterFactory outputWriterFactory;
    private final DependencyVerifierFactory fallbackVerifierFactory;
    private final ChecksumCalculator checksumCalculator;

    public ChecksumDependencyVerifierFactory(final OutputWriterFactory outputWriterFactory, final DependencyVerifierFactory fallbackVerifierFactory, final ChecksumCalculator checksumCalculator) {
        this.outputWriterFactory = outputWriterFactory;
        this.fallbackVerifierFactory = fallbackVerifierFactory;
        this.checksumCalculator = checksumCalculator;
    }

    @Override
    public DependencyVerifier create(final DependencyResolver resolver) {
        return new ChecksumDependencyVerifier(resolver, outputWriterFactory, fallbackVerifierFactory.create(resolver), checksumCalculator);
    }
}
