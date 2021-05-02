package io.github.vshnv.slimjar.data.reader;

import com.google.gson.Gson;
import io.github.vshnv.slimjar.data.DependencyData;

import java.io.*;

public final class GsonDependencyReader implements DependencyReader {
    private final Gson gson;

    public GsonDependencyReader(Gson gson) {
        this.gson = gson;
    }

    @Override
    public DependencyData read(InputStream inputStream) {
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return gson.fromJson(inputStreamReader, DependencyData.class);
    }
}
