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

package io.github.slimjar.resolver.reader.dependency;

import io.github.slimjar.resolver.data.DependencyData;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
    public DependencyData get() throws IOException, ReflectiveOperationException {
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
            return dependencyReader.read(inputStream);
        }
    }
}
