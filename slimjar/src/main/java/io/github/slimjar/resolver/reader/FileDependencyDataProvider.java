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

package io.github.slimjar.resolver.reader;


import io.github.slimjar.resolver.data.DependencyData;

import java.io.*;
import java.net.URL;

public final class FileDependencyDataProvider implements DependencyDataProvider {
    private final DependencyReader dependencyReader;
    private final URL depFileURL;

    public FileDependencyDataProvider(final DependencyReader dependencyReader, final URL depFileURL) {
        this.dependencyReader = dependencyReader;
        this.depFileURL = depFileURL;
    }

    public DependencyReader getDependencyReader() {
        return dependencyReader;
    }

    @Override
    public DependencyData get() {
        try (InputStream is = depFileURL.openStream()) {
            return dependencyReader.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
