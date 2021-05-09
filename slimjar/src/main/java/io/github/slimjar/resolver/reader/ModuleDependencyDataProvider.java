package io.github.slimjar.resolver.reader;

import io.github.slimjar.resolver.data.DependencyData;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public final class ModuleDependencyDataProvider implements DependencyDataProvider {
    private final DependencyReader dependencyReader;
    private final URL moduleUrl;

    public ModuleDependencyDataProvider(final DependencyReader dependencyReader, final URL moduleUrl) {
        this.dependencyReader = dependencyReader;
        this.moduleUrl = moduleUrl;
    }

    @Override
    public DependencyData get() throws IOException {
        final URL depFileURL = new URL("jar:file:" +moduleUrl.getFile() +"!/slimjar.json");
        final URLConnection connection = depFileURL.openConnection();
        if (!(connection instanceof JarURLConnection)) {
            throw new AssertionError("Invalid Module URL provided(Non-Jar File)");
        }
        final JarURLConnection jarURLConnection = (JarURLConnection) connection;
        final JarFile jarFile = jarURLConnection.getJarFile();
        final ZipEntry dependencyFileEntry = jarFile.getEntry("slimjar.json");
        if (dependencyFileEntry == null) {
            return new DependencyData(
                    Collections.emptySet(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    Collections.emptySet()
            );
        }
        try (InputStream inputStream = jarFile.getInputStream(dependencyFileEntry)){
            final Path file = Files.createTempFile("test", ".json");
            Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
        }
        try (InputStream inputStream = jarFile.getInputStream(dependencyFileEntry)){
            return dependencyReader.read(inputStream);
        }
    }
}
