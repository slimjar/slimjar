package io.github.slimjar.downloader.verify;

import io.github.slimjar.downloader.output.OutputWriter;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.ResolutionResult;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.util.Connections;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ChecksumDependencyVerifier implements DependencyVerifier {
    private static final Logger LOGGER = Logger.getLogger(ChecksumDependencyVerifier.class.getName());
    private final DependencyResolver resolver;
    private final OutputWriterFactory outputWriterFactory;
    private final DependencyVerifier fallbackVerifier;
    private final ChecksumCalculator checksumCalculator;

    public ChecksumDependencyVerifier(final DependencyResolver resolver, final OutputWriterFactory outputWriterFactory, final DependencyVerifier fallbackVerifier, final ChecksumCalculator checksumCalculator) {
        this.resolver = resolver;
        this.outputWriterFactory = outputWriterFactory;
        this.fallbackVerifier = fallbackVerifier;
        this.checksumCalculator = checksumCalculator;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean verify(final File file, final Dependency dependency) throws IOException {
        if (!file.exists()) return false;
        LOGGER.log(Level.FINE, "Attempting to verify file checksum for {0}", dependency.getArtifactId());
        final File checksumFile = outputWriterFactory.getStrategy().selectFileFor(dependency);
        checksumFile.getParentFile().mkdirs();
        if (!checksumFile.exists() && !prepareChecksumFile(checksumFile, dependency)) {
            LOGGER.log(Level.FINE, "Unable to resolve on checksum attempt {0}", dependency.getArtifactId());
            return false;
        }
        if (checksumFile.length() == 0L) {
            LOGGER.log(Level.FINE, "Required checksum not found for {0}, using fallback verifier", dependency.getArtifactId());

            return fallbackVerifier.verify(file, dependency);
        }
        final String actualChecksum = checksumCalculator.calculate(file);
        final String expectedChecksum = new String(Files.readAllBytes(checksumFile.toPath()));
        LOGGER.log(Level.FINE, "Actual checksum: {0}; Expected checksum: {1} for {2}", new Object[]{actualChecksum, expectedChecksum, dependency.getArtifactId()});
        return Objects.equals(actualChecksum, expectedChecksum);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean prepareChecksumFile(final File checksumFile, final Dependency dependency) throws IOException {
        final Optional<ResolutionResult> result = resolver.resolve(dependency);
        if (!result.isPresent()) {
            return false;
        } else {
            final URL checkSumUrl = result.get().getChecksumURL();
            LOGGER.log(Level.FINEST, "Resolved checksum URL for {0} as {1}", new Object[] {dependency.getArtifactId(), checkSumUrl});
            if (checkSumUrl == null) {
                checksumFile.createNewFile();
                return true;
            }
            final URLConnection connection = Connections.createDownloadConnection(checkSumUrl);
            final InputStream inputStream = connection.getInputStream();
            final OutputWriter outputWriter = outputWriterFactory.create(dependency);
            outputWriter.writeFrom(inputStream, connection.getContentLength());
            Connections.tryDisconnect(connection);
            LOGGER.log(Level.FINEST, "Downloaded checksum for {0}", dependency.getArtifactId());
        }
        return true;
    }
}
