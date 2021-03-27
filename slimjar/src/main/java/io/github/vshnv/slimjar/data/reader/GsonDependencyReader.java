package io.github.vshnv.slimjar.data.reader;

import com.google.gson.Gson;
import io.github.vshnv.slimjar.data.DependencyData;

import java.io.*;
import java.util.function.Function;

public final class GsonDependencyReader implements DependencyReader {
    private final Function<Void, InputStream> inputStreamProducer;
    private final Gson gson;

    public GsonDependencyReader(Function<Void, InputStream> inputStreamProducer, Gson gson) {
        this.inputStreamProducer = inputStreamProducer;
        this.gson = gson;
    }

    @Override
    public DependencyData read() {
        try (InputStream stream = inputStreamProducer.apply(null)) {
            final InputStreamReader inputStreamReader = new InputStreamReader(stream);
            return gson.fromJson(inputStreamReader, DependencyData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
