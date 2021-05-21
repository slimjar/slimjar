package io.github.slimjar.util;

import io.github.slimjar.app.ApplicationFactory;
import io.github.slimjar.app.module.ModuleExtractor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class Modules {
    private Modules() {
    }

    public static URL findModule(final String moduleName) {
        final ClassLoader classLoader = ApplicationFactory.class.getClassLoader();
        return classLoader.getResource(moduleName + ".isolated-jar");
    }

    public static URL[] extract(final ModuleExtractor extractor, final Collection<String> modules) throws IOException {
        final Collection<URL> result = new HashSet<>();
        for (final String moduleName : modules) {
            final URL modulePath = findModule(moduleName);
            final URL extractedModule = extractor.extractModule(modulePath, moduleName);
            result.add(extractedModule);
        }
        return result.toArray(new URL[0]);
    }

    public static Collection<String> findLocalModules() throws URISyntaxException, IOException {
        URL url = Modules.class.getResource("/");
        Path resourcesPath = Paths.get(url.toURI());
        return Files.walk(resourcesPath, 1)
                .filter(path -> path.endsWith(".isolated-jar"))
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toSet());
    }
}
