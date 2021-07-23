package io.github.slimjar.resolver.reader.resolution;

import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.dependency.DependencyReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public final class GsonPreResolutionDataProvider implements PreResolutionDataProvider {
    private final PreResolutionDataReader resolutionDataReader;
    private final URL resolutionFileURL;
    private Map<String, URL> cachedData = null;

    public GsonPreResolutionDataProvider(final PreResolutionDataReader resolutionDataReader, final URL resolutionFileURL) {
        this.resolutionDataReader = resolutionDataReader;
        this.resolutionFileURL = resolutionFileURL;
    }

    @Override
    public Map<String, URL> get() throws IOException, ReflectiveOperationException {
        if (cachedData != null) {
            return cachedData;
        }
        try (InputStream is = resolutionFileURL.openStream()) {
            cachedData = resolutionDataReader.read(is);
            return cachedData;
        }
    }
}
