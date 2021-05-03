package io.github.vshnv.slimjar.resolver.reader;


import io.github.vshnv.slimjar.resolver.data.DependencyData;

import java.io.*;
import java.net.URL;

public final class FileDependencyDataProvider implements DependencyDataProvider {
    private final DependencyReader dependencyReader;
    private final URL depFileURL;

    public FileDependencyDataProvider(final DependencyReader dependencyReader, final URL depFileURL) {
        this.dependencyReader = dependencyReader;
        this.depFileURL = depFileURL;
    }

    public DependencyReader getDependencyReader() {
        return dependencyReader;
    }

    @Override
    public DependencyData get() {
        try (InputStream is = depFileURL.openStream()) {
            return dependencyReader.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
