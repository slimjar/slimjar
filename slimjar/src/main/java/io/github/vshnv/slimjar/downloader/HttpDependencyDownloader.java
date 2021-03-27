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

    public HttpDependencyDownloader(Function<String, OutputWriter> outputWriterProducer) {
        this.outputWriterProducer = outputWriterProducer;
    }

    @Override
    public URL download(Dependency dependency) throws IOException {
        URL url = new URL(dependency.getUrl());
        HttpURLConnection httpConnection = createDownloadConnection(url);
        String fileName = dependency.getName() + ".jar";
        InputStream inputStream = httpConnection.getInputStream();
        OutputWriter outputWriter = outputWriterProducer.apply(fileName);
        outputWriter.writeFrom(inputStream, httpConnection.getContentLength());
        httpConnection.disconnect();
        return url;
    }

    private HttpURLConnection createDownloadConnection(URL url) throws IOException {
        URLConnection connection =  url.openConnection();
        if (!Objects.equals(url.getProtocol(), "HTTP")) {
            throw new IOException("Unexpected protocol. Expecting Http, URL was: " + url);
        }
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        int responseCode = httpConnection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Could not download from" + url);
        }
        return httpConnection;
    }
}
