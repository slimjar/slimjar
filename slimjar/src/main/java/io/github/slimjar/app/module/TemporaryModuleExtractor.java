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

package io.github.slimjar.app.module;

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
        tempFile.deleteOnExit();
        final URLConnection connection = url.openConnection();
        if (!(connection instanceof JarURLConnection)) {
            throw new AssertionError("Invalid Module URL provided(Non-Jar File)");
        }
        final JarURLConnection jarURLConnection = (JarURLConnection) connection;
        final JarFile jarFile = jarURLConnection.getJarFile();
        final ZipEntry module = jarFile.getJarEntry(name + ".isolated-jar");
        if (module == null) {
            throw new ModuleNotFoundException(name);
        }
        try (final InputStream inputStream = jarFile.getInputStream(module)) {
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile.toURI().toURL();
    }
}
