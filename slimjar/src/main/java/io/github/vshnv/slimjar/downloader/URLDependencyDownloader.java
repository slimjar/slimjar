package io.github.vshnv.slimjar.downloader;

import io.github.vshnv.slimjar.downloader.output.OutputWriter;
import io.github.vshnv.slimjar.downloader.output.OutputWriterFactory;
import io.github.vshnv.slimjar.resolver.DependencyResolver;
import io.github.vshnv.slimjar.resolver.data.Dependency;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public final class URLDependencyDownloader implements DependencyDownloader {
    private final OutputWriterFactory outputWriterProducer;
    private final DependencyResolver dependencyResolver;
    public URLDependencyDownloader(final OutputWriterFactory outputWriterProducer, DependencyResolver dependencyResolver) {
        this.outputWriterProducer = outputWriterProducer;
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public URL download(final Dependency dependency) throws IOException {
        final URL url = dependencyResolver.resolve(dependency);
        final URLConnection connection = createDownloadConnection(url);
        final InputStream inputStream = connection.getInputStream();
        final OutputWriter outputWriter = outputWriterProducer.create(dependency);
        outputWriter.writeFrom(inputStream, connection.getContentLength());
        tryDisconnect(connection);
        return url;
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
