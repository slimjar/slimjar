package io.github.vshnv.slimjar.resolver.reader;


import io.github.vshnv.slimjar.resolver.DependencyData;

import java.io.InputStream;

public interface DependencyReader {
    DependencyData read(InputStream inputStream);
}
