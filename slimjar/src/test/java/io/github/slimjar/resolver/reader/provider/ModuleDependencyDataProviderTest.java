package io.github.slimjar.resolver.reader.provider;

import io.github.slimjar.reader.GsonDependencyReader;
import com.google.gson.Gson;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.DependencyDataProvider;
import io.github.slimjar.resolver.reader.MockDependencyData;
import io.github.slimjar.resolver.reader.ModuleDependencyDataProvider;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, ModuleDependencyDataProviderTest.class, ModuleDependencyDataProvider.class})
public class ModuleDependencyDataProviderTest extends TestCase {
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
        final DependencyDataProvider dependencyDataProvider = new ModuleDependencyDataProvider(new GsonDependencyReader(new Gson()), mockUrl);
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
        final DependencyDataProvider dependencyDataProvider = new ModuleDependencyDataProvider(new GsonDependencyReader(new Gson()), mockUrl);
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
        final DependencyDataProvider dependencyDataProvider = new ModuleDependencyDataProvider(new GsonDependencyReader(new Gson()), mockUrl);
        Error error = null;
        try {
            dependencyDataProvider.get();
        } catch (Error thrown) {
            error = thrown;
        }
        assertTrue("Non-Jar urlcorrection should throw AssertionError", error instanceof AssertionError);
    }
}
