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

package io.github.slimjar.injector;

import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.injector.loader.Injectable;
import io.github.slimjar.relocation.RelocationHelper;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.resolver.data.Dependency;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public final class SimpleDependencyInjector implements DependencyInjector {
    private final DependencyDownloader dependencyDownloader;
    private final RelocationHelper relocationHelper;

    public SimpleDependencyInjector(final DependencyDownloader dependencyDownloader, final RelocationHelper relocationHelper) {
        this.dependencyDownloader = dependencyDownloader;
        this.relocationHelper = relocationHelper;
    }

    @Override
    public void inject(final Injectable injectable, final Collection<Dependency> dependencies) {
        for (final Dependency dependency : dependencies) {
            try {
                final File downloaded = dependencyDownloader.download(dependency);
                final File relocatedFile = relocationHelper.relocate(dependency, downloaded);
                injectable.inject(relocatedFile.toURI().toURL());
                inject(injectable, dependency.getTransitive());
            } catch (final IOException e) {
                throw new InjectionFailedException(dependency, e);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
