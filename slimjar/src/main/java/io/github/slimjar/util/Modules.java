package io.github.slimjar.util;

import io.github.slimjar.app.module.ModuleExtractor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

public final class Modules {
    private Modules() {
    }

    public static URL findModule(final String moduleName) {
        final ClassLoader classLoader = Modules.class.getClassLoader();
        return classLoader.getResource(moduleName + ".isolated-jar");
    }

    public static URL[] extract(final ModuleExtractor extractor, final Collection<String> modules) throws IOException {
        final URL[] urls = new URL[modules.size()];
        int index = 0;
        for (final String moduleName : modules) {
            final URL modulePath = findModule(moduleName);
            final URL extractedModule = extractor.extractModule(modulePath, moduleName);
            urls[index++] = extractedModule;
        }
        return urls;
    }

    public static Collection<String> findLocalModules() throws URISyntaxException, IOException {
        final URL url = Modules.class.getProtectionDomain().getCodeSource().getLocation();
        final Path resourcesPath = Paths.get(url.toURI());
        return Files.walk(resourcesPath, 1)
                .filter(path -> path.endsWith(".isolated-jar"))
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toSet());
    }
}
