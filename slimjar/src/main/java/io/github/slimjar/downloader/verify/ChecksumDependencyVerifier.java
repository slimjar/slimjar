//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package io.github.slimjar.downloader.verify;

import io.github.slimjar.downloader.output.OutputWriter;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.logging.LogDispatcher;
import io.github.slimjar.logging.ProcessLogger;
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
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ChecksumDependencyVerifier implements DependencyVerifier {
    private static final ProcessLogger LOGGER = LogDispatcher.getMediatingLogger();
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
        LOGGER.log("Verifying checksum for {0}", dependency.getArtifactId());
        final File checksumFile = outputWriterFactory.getStrategy().selectFileFor(dependency);
        checksumFile.getParentFile().mkdirs();
        if (!checksumFile.exists() && !prepareChecksumFile(checksumFile, dependency)) {
            LOGGER.log("Unable to resolve checksum for {0}, falling back to fallbackVerifier!", dependency.getArtifactId());
            return fallbackVerifier.verify(file, dependency);
        }
        if (checksumFile.length() == 0L) {
            LOGGER.log("Required checksum not found for {0}, using fallbackVerifier!", dependency.getArtifactId());
            return fallbackVerifier.verify(file, dependency);
        }
        final String actualChecksum = checksumCalculator.calculate(file);
        final String expectedChecksum = new String(Files.readAllBytes(checksumFile.toPath())).trim();
        LOGGER.debug("{0} -> Actual checksum: {1};", dependency.getArtifactId(), actualChecksum);
        LOGGER.debug("{0} -> Expected checksum: {1};", dependency.getArtifactId(), expectedChecksum);
        final boolean match = Objects.equals(actualChecksum, expectedChecksum);
        LOGGER.log("Checksum {0} for {1}", match ? "matched" : "match failed", dependency.getArtifactId());
        return Objects.equals(actualChecksum, expectedChecksum);
    }

    @Override
    public File getChecksumFile(final Dependency dependency) {
        final File checksumFile = outputWriterFactory.getStrategy().selectFileFor(dependency);
        checksumFile.getParentFile().mkdirs();
        return checksumFile;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean prepareChecksumFile(final File checksumFile, final Dependency dependency) throws IOException {
        final Optional<ResolutionResult> result = resolver.resolve(dependency);
        if (!result.isPresent()) {
            return false;
        } else {
            final URL checkSumUrl = result.get().getChecksumURL();
            LOGGER.log("Resolved checksum URL for {0} as {1}", dependency.getArtifactId(), checkSumUrl);
            if (checkSumUrl == null) {
                checksumFile.createNewFile();
                return true;
            }
            final URLConnection connection = Connections.createDownloadConnection(checkSumUrl);
            final InputStream inputStream = connection.getInputStream();
            final OutputWriter outputWriter = outputWriterFactory.create(dependency);
            outputWriter.writeFrom(inputStream, connection.getContentLength());
            Connections.tryDisconnect(connection);
            LOGGER.log("Downloaded checksum for {0}", dependency.getArtifactId());
        }
        return true;
    }
}
