package io.github.vshnv.slimjar.resolver.pinger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

public final class HttpURLPinger implements URLPinger {
    private static final String SLIMJAR_USER_AGENT = "SlimjarApplication/* URL Validation Ping";
    private static final Collection<String> SUPPOURTED_PROTOCOLS = Arrays.asList("HTTP", "HTTPS");

    @Override
    public boolean ping(final URL url) {
        if (!isSupported(url)) {
            return false;
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", SLIMJAR_USER_AGENT);
            connection.connect();
            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public boolean isSupported(final URL url) {
        final String protocol = url.getProtocol().toUpperCase();
        return SUPPOURTED_PROTOCOLS.contains(protocol);
    }
}
