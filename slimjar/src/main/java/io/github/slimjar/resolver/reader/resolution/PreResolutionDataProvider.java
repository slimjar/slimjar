package io.github.slimjar.resolver.reader.resolution;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@FunctionalInterface
public interface PreResolutionDataProvider {
    Map<String, URL> get() throws IOException, ReflectiveOperationException;
}
