//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

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
