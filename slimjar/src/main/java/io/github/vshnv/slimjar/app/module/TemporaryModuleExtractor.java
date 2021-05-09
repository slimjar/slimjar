package io.github.vshnv.slimjar.app.module;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public final class TemporaryModuleExtractor implements ModuleExtractor {
    @Override
    public URL extractModule(URL url, String name) throws IOException {
        final File tempFile = File.createTempFile(name, ".jar");
        final URLConnection connection = url.openConnection();
        if (!(connection instanceof JarURLConnection)) {
            throw new AssertionError("Invalid Module URL provided(Non-Jar File)");
        }
        final JarURLConnection jarURLConnection = (JarURLConnection) connection;
        final JarFile jarFile = jarURLConnection.getJarFile();
        final ZipEntry module = jarFile.getJarEntry(name + ".isolated-jar");
        try (final InputStream inputStream = jarFile.getInputStream(module)) {
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile.toURI().toURL();
    }
}
