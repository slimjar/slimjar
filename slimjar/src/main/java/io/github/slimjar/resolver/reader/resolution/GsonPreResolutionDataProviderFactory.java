package io.github.slimjar.resolver.reader.resolution;

import io.github.slimjar.resolver.reader.facade.GsonFacade;
import io.github.slimjar.resolver.reader.facade.GsonFacadeFactory;

import java.net.URL;

public final class GsonPreResolutionDataProviderFactory implements PreResolutionDataProviderFactory {
    private final GsonFacade gson;

    public GsonPreResolutionDataProviderFactory(final GsonFacadeFactory gson) throws ReflectiveOperationException {
        this(gson.createFacade());
    }

    public GsonPreResolutionDataProviderFactory(final GsonFacade gson) {
        this.gson = gson;
    }

    @Override
    public PreResolutionDataProvider create(final URL resolutionFileURL) {
        final PreResolutionDataReader resolutionDataReader = new GsonPreResolutionDataReader(gson);
        return new GsonPreResolutionDataProvider(resolutionDataReader, resolutionFileURL);
    }
}
