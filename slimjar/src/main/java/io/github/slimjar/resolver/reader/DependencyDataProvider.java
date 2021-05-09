package io.github.slimjar.resolver.reader;


import io.github.slimjar.resolver.data.DependencyData;

import java.io.IOException;


public interface DependencyDataProvider {
    DependencyData get() throws IOException;
}
