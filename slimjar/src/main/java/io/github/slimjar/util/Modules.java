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
