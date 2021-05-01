package io.github.vshnv.slimjar.resolver.pinger;

import java.net.URL;

public interface URLPinger {
    boolean ping(final URL url);
    boolean isSupported(final URL url);
}
