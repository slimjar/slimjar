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

import io.github.slimjar.downloader.output.ChanneledFileOutputWriter;
import io.github.slimjar.downloader.output.OutputWriter;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.UnresolvedDependencyException;
import io.github.slimjar.resolver.data.Dependency;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class URLDependencyDownloader implements DependencyDownloader {
    private static final Logger LOGGER = Logger.getLogger(URLDependencyDownloader.class.getName());
    private final OutputWriterFactory outputWriterProducer;
    private final DependencyResolver dependencyResolver;
    public URLDependencyDownloader(final OutputWriterFactory outputWriterProducer, DependencyResolver dependencyResolver) {
        this.outputWriterProducer = outputWriterProducer;
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public File download(final Dependency dependency) throws IOException {
        final URL url = dependencyResolver.resolve(dependency)
                .orElseThrow(() -> new UnresolvedDependencyException(dependency));
        LOGGER.log(Level.FINE, "Connecting to {0}", url);
        final URLConnection connection = createDownloadConnection(url);
        final InputStream inputStream = connection.getInputStream();
        LOGGER.log(Level.FINE, "Connection successful! Downloading {0}" ,dependency.getArtifactId() + "...");
        final OutputWriter outputWriter = outputWriterProducer.create(dependency);
        final File result = outputWriter.writeFrom(inputStream, connection.getContentLength());
        tryDisconnect(connection);
        LOGGER.log(Level.FINE, "Artifact {0} downloaded successfully!", dependency.getArtifactId());
        return result;
    }

    private URLConnection createDownloadConnection(final URL url) throws IOException {
        final URLConnection connection =  url.openConnection();
        if (connection instanceof HttpURLConnection) {
            final HttpURLConnection httpConnection = (HttpURLConnection) connection;
            final int responseCode = httpConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Could not download from" + url);
            }
        }
        return connection;
    }
    private void tryDisconnect(final URLConnection urlConnection) {
        if (urlConnection instanceof HttpURLConnection) {
            ((HttpURLConnection) urlConnection).disconnect();
        }
    }
}
