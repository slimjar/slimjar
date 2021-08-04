package io.github.slimjar.resolver.reader.resolution;

import io.github.slimjar.resolver.ResolutionResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

public final class GsonPreResolutionDataProvider implements PreResolutionDataProvider {
    private final PreResolutionDataReader resolutionDataReader;
    private final URL resolutionFileURL;
    private Map<String, ResolutionResult> cachedData = null;

    public GsonPreResolutionDataProvider(final PreResolutionDataReader resolutionDataReader, final URL resolutionFileURL) {
        this.resolutionDataReader = resolutionDataReader;
        this.resolutionFileURL = resolutionFileURL;
    }

    @Override
    public Map<String, ResolutionResult> get() throws IOException, ReflectiveOperationException {
        if (cachedData != null) {
            return cachedData;
        }
        try (InputStream is = resolutionFileURL.openStream()) {
            cachedData = resolutionDataReader.read(is);
            return cachedData;
        } catch (final Exception exception) {
            return Collections.emptyMap();
        }
    }
}
