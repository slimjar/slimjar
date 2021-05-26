package io.github.slimjar.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public final class Connections {
    private Connections() {

    }

    public static URLConnection createDownloadConnection(final URL url) throws IOException {
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
    public static void tryDisconnect(final URLConnection urlConnection) {
        if (urlConnection instanceof HttpURLConnection) {
            ((HttpURLConnection) urlConnection).disconnect();
        }
    }
}
