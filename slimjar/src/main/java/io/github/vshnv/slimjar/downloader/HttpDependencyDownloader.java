package io.github.vshnv.slimjar.downloader;

import io.github.vshnv.slimjar.data.Dependency;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.function.Function;

public final class HttpDependencyDownloader implements DependencyDownloader {
    private final Function<String, OutputWriter> outputWriterProducer;

    public HttpDependencyDownloader(final Function<String, OutputWriter> outputWriterProducer) {
        this.outputWriterProducer = outputWriterProducer;
    }

    @Override
    public URL download(final Dependency dependency) throws IOException {
        final URL url = new URL(dependency.getUrl());
        final HttpURLConnection httpConnection = createDownloadConnection(url);
        final String fileName = dependency.getName() + ".jar";
        final InputStream inputStream = httpConnection.getInputStream();
        final OutputWriter outputWriter = outputWriterProducer.apply(fileName);
        outputWriter.writeFrom(inputStream, httpConnection.getContentLength());
        httpConnection.disconnect();
        return url;
    }

    private HttpURLConnection createDownloadConnection(final URL url) throws IOException {
        final URLConnection connection =  url.openConnection();
        if (!Objects.equals(url.getProtocol(), "HTTP")) {
            throw new IOException("Unexpected protocol. Expecting Http, URL was: " + url);
        }
        final HttpURLConnection httpConnection = (HttpURLConnection) connection;
        final int responseCode = httpConnection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Could not download from" + url);
        }
        return httpConnection;
    }
}
