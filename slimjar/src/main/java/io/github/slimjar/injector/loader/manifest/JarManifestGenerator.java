package io.github.slimjar.injector.loader.manifest;

import java.io.IOException;
import java.io.Writer;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public final class JarManifestGenerator implements ManifestGenerator {
    private final Map<String, String> attributes = new HashMap<>();
    private final URI jarURI;

    public JarManifestGenerator(final URI jarURI) {
        this.jarURI = jarURI;
    }

    @Override
    public ManifestGenerator attribute(final String key, final String value) {
        attributes.put(key, value);
        return this;
    }

    @Override
    public void generate() throws IOException {
        final Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        final URI uri = URI.create(String.format("jar:%s", jarURI));
        try (final FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            final Path nf = fs.getPath("META-INF/MANIFEST.MF");
            Files.createDirectories(nf.getParent());
            try (final Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
               for (Map.Entry<String, String> entry : attributes.entrySet()) {
                   writer.write(String.format("%s: %s%n", entry.getKey(), entry.getValue()));
               }
            }
        }
    }

    public static ManifestGenerator with(final URI uri) {
        return new JarManifestGenerator(uri);
    }
}
