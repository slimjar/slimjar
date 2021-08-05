package io.github.slimjar.resolver.reader.resolution;

import io.github.slimjar.resolver.ResolutionResult;
import io.github.slimjar.resolver.reader.facade.GsonFacade;
import io.github.slimjar.resolver.reader.facade.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;

public final class GsonPreResolutionDataReader implements PreResolutionDataReader {

    private final GsonFacade gson;

    public GsonPreResolutionDataReader(final GsonFacade gson) {
        this.gson = gson;
    }

    @Override
    public Map<String, ResolutionResult> read(final InputStream inputStream) throws IOException, ReflectiveOperationException {
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final Type rawType = new TypeToken<Map<String, ResolutionResult>>(){}.getRawType();
        return gson.fromJson(inputStreamReader, rawType);
    }
}
