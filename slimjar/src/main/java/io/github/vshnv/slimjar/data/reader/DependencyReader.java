package io.github.vshnv.slimjar.data.reader;

import io.github.vshnv.slimjar.data.DependencyData;

import java.io.InputStream;

public interface DependencyReader {
    DependencyData read(InputStream inputStream);
}
