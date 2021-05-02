package io.github.vshnv.slimjar.data.reader;

import io.github.vshnv.slimjar.data.DependencyData;

import java.io.*;

public final class FileDependencyDataProvider implements DependencyDataProvider {
    private final DependencyReader dependencyReader;
    private final File depFile;

    public FileDependencyDataProvider(final DependencyReader dependencyReader, final File depFile) {
        this.dependencyReader = dependencyReader;
        this.depFile = depFile;
    }

    public DependencyReader getDependencyReader() {
        return dependencyReader;
    }

    @Override
    public DependencyData get() {
        try (InputStream is = new FileInputStream(depFile)) {
            return dependencyReader.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
