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

import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.reader.dependency.*;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;

public class DependencyDataProviderFactoryTest extends TestCase {
    private static final Path DEFAULT_DOWNLOAD_DIRECTORY;
    private static final Collection<Repository> CENTRAL_MIRRORS;

    static {
        try {
            CENTRAL_MIRRORS = Collections.singleton(new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL)));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        final String userHome = System.getProperty("user.home");
        final String defaultPath = String.format("%s/.slimjar", userHome);
        DEFAULT_DOWNLOAD_DIRECTORY = new File(defaultPath).toPath();
    }


    public void testCreateFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");
        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS));
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("create must return a FileDependencyDataProvider", provider instanceof URLDependencyDataProvider);
    }

    public void testCreateFileDataProviderFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");
        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS));
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("forFile must return a FileDependencyDataProvider", provider instanceof URLDependencyDataProvider);
    }

    public void testCreateModuleDataProviderFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");

        final DependencyDataProviderFactory dependencyDataProviderFactory = new ExternalDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS));
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("forFile must return a FileDependencyDataProvider", provider instanceof ModuleDependencyDataProvider);
    }
}