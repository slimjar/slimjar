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

package io.github.slimjar.downloader;

import io.github.slimjar.downloader.output.OutputWriter;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.downloader.verify.DependencyVerifier;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.ResolutionResult;
import io.github.slimjar.resolver.UnresolvedDependencyException;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.util.Connections;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class URLDependencyDownloader implements DependencyDownloader {
    private static final Logger LOGGER = Logger.getLogger(URLDependencyDownloader.class.getName());
    private final OutputWriterFactory outputWriterProducer;
    private final DependencyResolver dependencyResolver;
    private final DependencyVerifier verifier;

    public URLDependencyDownloader(final OutputWriterFactory outputWriterProducer, DependencyResolver dependencyResolver, DependencyVerifier verifier) {
        this.outputWriterProducer = outputWriterProducer;
        this.dependencyResolver = dependencyResolver;
        this.verifier = verifier;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public File download(final Dependency dependency) throws IOException {
        final ResolutionResult result = dependencyResolver.resolve(dependency)
                .orElseThrow(() -> new UnresolvedDependencyException(dependency));
        final File expectedOutputFile = outputWriterProducer.getStrategy().selectFileFor(dependency);
        if (verifier.verify(expectedOutputFile, dependency)) {
            return expectedOutputFile;
        }
        expectedOutputFile.delete();
        final URL url = result.getDependencyURL();
        LOGGER.log(Level.FINE, "Connecting to {0}", url);
        final URLConnection connection = Connections.createDownloadConnection(url);
        final InputStream inputStream = connection.getInputStream();
        LOGGER.log(Level.FINE, "Connection successful! Downloading {0}" ,dependency.getArtifactId() + "...");
        final OutputWriter outputWriter = outputWriterProducer.create(dependency);
        final File downloadResult = outputWriter.writeFrom(inputStream, connection.getContentLength());
        Connections.tryDisconnect(connection);
        LOGGER.log(Level.FINE, "Artifact {0} downloaded successfully!", dependency.getArtifactId());
        return downloadResult;
    }
}
