package io.github.slimjar.resolver.reader;

import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.facade.GsonFacade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class GsonDependencyReader implements DependencyReader {
    private final GsonFacade gson;

    public GsonDependencyReader(final GsonFacade gson) {
        this.gson = gson;
    }

    @Override
    public DependencyData read(final InputStream inputStream) throws ReflectiveOperationException {
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return gson.fromJson(inputStreamReader, DependencyData.class);
    }
}
