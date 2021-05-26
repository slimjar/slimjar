package io.github.slimjar.downloader.verify;

import io.github.slimjar.downloader.output.OutputWriter;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.ResolutionResult;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.util.Connections;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Optional;

public final class ChecksumDependencyVerifier implements DependencyVerifier {
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
        final File checksumFile = outputWriterFactory.getStrategy().selectFileFor(dependency);
        checksumFile.getParentFile().mkdirs();
        if (!checksumFile.exists() && !prepareChecksumFile(checksumFile, dependency)) {
            return false;
        }
        if (checksumFile.length() == 0L) {
            return fallbackVerifier.verify(file, dependency);
        }
        return checksumCalculator.calculate(file).equals(new String(Files.readAllBytes(checksumFile.toPath())));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean prepareChecksumFile(final File checksumFile, final Dependency dependency) throws IOException {
        final Optional<ResolutionResult> result = resolver.resolve(dependency);
        if (!result.isPresent()) {
            return false;
        } else {
            final URL checkSumUrl = result.get().getChecksumURL();
            if (checkSumUrl == null) {
                checksumFile.createNewFile();
                return true;
            }
            final URLConnection connection = Connections.createDownloadConnection(checkSumUrl);
            final InputStream inputStream = connection.getInputStream();
            final OutputWriter outputWriter = outputWriterFactory.create(dependency);
            outputWriter.writeFrom(inputStream, connection.getContentLength());
            Connections.tryDisconnect(connection);
        }
        return true;
    }
}
