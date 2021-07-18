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

package io.github.slimjar.resolver.pinger;

import io.github.slimjar.logging.LogDispatcher;
import io.github.slimjar.logging.ProcessLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

public final class HttpURLPinger implements URLPinger {
    private static final ProcessLogger LOGGER = LogDispatcher.getMediatingLogger();
    private static final String SLIMJAR_USER_AGENT = "SlimjarApplication/* URL Validation Ping";
    private static final Collection<String> SUPPORTED_PROTOCOLS = Arrays.asList("HTTP", "HTTPS");

    @Override
    public boolean ping(final URL url) {
        final String urlStr = url.toString();
        LOGGER.debug("Pinging {0}", urlStr);
        if (!isSupported(url)) {
            LOGGER.debug("Protocol not supported for {0}", url.toString());
            return false;
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", SLIMJAR_USER_AGENT);
            connection.connect();
            final boolean result = connection.getResponseCode() == HttpURLConnection.HTTP_OK;
            LOGGER.debug("Ping {1} for {0}", url.toString(), result ? "successful" : "failed");
            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            LOGGER.debug("Ping failed for {0}", url.toString());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public boolean isSupported(final URL url) {
        final String protocol = url.getProtocol().toUpperCase(Locale.ENGLISH);
        return SUPPORTED_PROTOCOLS.contains(protocol);
    }
}
