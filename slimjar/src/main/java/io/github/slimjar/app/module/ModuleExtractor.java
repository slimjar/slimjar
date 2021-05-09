package io.github.slimjar.app.module;

import java.io.IOException;
import java.net.URL;

public interface ModuleExtractor {
    URL extractModule(final URL url, final String name) throws IOException;
}
