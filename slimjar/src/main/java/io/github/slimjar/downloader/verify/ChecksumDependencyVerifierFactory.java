package io.github.slimjar.downloader.verify;

import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.resolver.DependencyResolver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ChecksumDependencyVerifierFactory implements DependencyVerifierFactory {
    private static final Logger LOGGER = Logger.getLogger(ChecksumDependencyVerifierFactory.class.getName());
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
        LOGGER.log(Level.FINEST, "Creating verifier...");
        return new ChecksumDependencyVerifier(resolver, outputWriterFactory, fallbackVerifierFactory.create(resolver), checksumCalculator);
    }
}
