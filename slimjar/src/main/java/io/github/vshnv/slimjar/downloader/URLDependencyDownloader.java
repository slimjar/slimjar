package io.github.vshnv.slimjar.downloader;

import io.github.vshnv.slimjar.data.Dependency;
import io.github.vshnv.slimjar.downloader.output.OutputWriter;
import io.github.vshnv.slimjar.downloader.output.OutputWriterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public final class URLDependencyDownloader implements DependencyDownloader {
    private final OutputWriterFactory outputWriterProducer;

    public URLDependencyDownloader(final OutputWriterFactory outputWriterProducer) {
        this.outputWriterProducer = outputWriterProducer;
    }

    @Override
    public URL download(final Dependency dependency) throws IOException {
        final URL url = new URL(dependency.getUrl());
        final URLConnection connection = createDownloadConnection(url);
        final String fileName = dependency.getName() + ".jar";
        final InputStream inputStream = connection.getInputStream();
        final OutputWriter outputWriter = outputWriterProducer.create(fileName);
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
