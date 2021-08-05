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

package io.github.slimjar.resolver.reader.provider;

import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.reader.dependency.DependencyDataProvider;
import io.github.slimjar.resolver.reader.dependency.GsonDependencyReader;
import io.github.slimjar.resolver.reader.MockDependencyData;
import io.github.slimjar.resolver.reader.dependency.ModuleDependencyDataProvider;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, ModuleDependencyDataProviderTest.class, ModuleDependencyDataProvider.class})
public class ModuleDependencyDataProviderTest extends TestCase {
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

    public void testModuleDependencyDataProviderNonEmpty() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        final JarURLConnection jarURLConnection = PowerMockito.mock(JarURLConnection.class);
        final JarFile jarFile = PowerMockito.mock(JarFile.class);
        final ZipEntry zipEntry = PowerMockito.mock(ZipEntry.class);
        final InputStream inputStream = mockDependencyData.getDependencyDataInputStream();
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openConnection()).thenReturn(jarURLConnection);
        PowerMockito.when(jarURLConnection.getJarFile()).thenReturn(jarFile);
        PowerMockito.when(jarFile.getEntry("slimjar.json")).thenReturn(zipEntry);
        PowerMockito.when(jarFile.getInputStream(zipEntry)).thenReturn(inputStream);
        final DependencyDataProvider dependencyDataProvider = new ModuleDependencyDataProvider(new GsonDependencyReader(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS).createFacade()), mockUrl);
        assertEquals("Read and provide proper dependencies", mockDependencyData.getExpectedSample(), dependencyDataProvider.get());
    }

    public void testModuleDependencyDataProviderEmpty() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        final JarURLConnection jarURLConnection = PowerMockito.mock(JarURLConnection.class);
        final JarFile jarFile = PowerMockito.mock(JarFile.class);
        final DependencyData emptyDependency = new DependencyData(
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openConnection()).thenReturn(jarURLConnection);
        PowerMockito.when(jarURLConnection.getJarFile()).thenReturn(jarFile);
        PowerMockito.when(jarFile.getEntry("slimjar.json")).thenReturn(null);
        final DependencyDataProvider dependencyDataProvider = new ModuleDependencyDataProvider(new GsonDependencyReader(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS).createFacade()), mockUrl);
        assertEquals("Empty dependency if not exists", emptyDependency, dependencyDataProvider.get());
    }

    public void testModuleDependencyDataProviderExceptionIfNonJar() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        final HttpURLConnection urlConnection = PowerMockito.mock(HttpURLConnection.class);
        final JarFile jarFile = PowerMockito.mock(JarFile.class);
        final DependencyData emptyDependency = new DependencyData(
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openConnection()).thenReturn(urlConnection);
        final DependencyDataProvider dependencyDataProvider = new ModuleDependencyDataProvider(new GsonDependencyReader(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS).createFacade()), mockUrl);
        Error error = null;
        try {
            dependencyDataProvider.get();
        } catch (Error thrown) {
            error = thrown;
        }
        assertTrue("Non-Jar urlcorrection should throw AssertionError", error instanceof AssertionError);
    }
}
