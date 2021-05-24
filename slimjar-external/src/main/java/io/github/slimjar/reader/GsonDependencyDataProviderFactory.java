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

package io.github.slimjar.reader;

import com.google.gson.Gson;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;
import io.github.slimjar.resolver.reader.*;

import java.net.URL;

public final class GsonDependencyDataProviderFactory implements DependencyDataProviderFactory {
    private final Gson gson;

    public GsonDependencyDataProviderFactory(final Gson gson) {
        this.gson = gson;
    }

    /**
     * @see GsonDependencyDataProviderFactory#forFile(URL)
     * @param dependencyFileURL URL of dependency data file
     * @return DependencyDataProvider for given URL
     */
    @Deprecated
    public DependencyDataProvider create(final URL dependencyFileURL) {
        return forFile(dependencyFileURL);
    }

    public DependencyDataProvider forFile(final URL dependencyFileURL) {
        final DependencyReader dependencyReader = new GsonDependencyReader(gson);
        return new URLDependencyDataProvider(dependencyReader, dependencyFileURL);
    }

    public DependencyDataProvider forModule(final URL dependencyFileURL) {
        final DependencyReader dependencyReader = new GsonDependencyReader(gson);
        return new ModuleDependencyDataProvider(dependencyReader, dependencyFileURL);
    }
}
