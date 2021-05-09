package io.github.vshnv.slimjar.resolver.reader;


import io.github.vshnv.slimjar.resolver.data.DependencyData;

import java.io.IOException;


public interface DependencyDataProvider {
    DependencyData get() throws IOException;
}
