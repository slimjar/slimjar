package io.github.slimjar.resolver.reader.provider;

import com.google.gson.Gson;
import io.github.slimjar.reader.GsonDependencyReader;
import io.github.slimjar.resolver.reader.*;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.URL;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, URLDependencyDataProvider.class})
public class URLDependencyDataProviderTest extends TestCase {
    public void testFileDependencyDataProvider() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments("MyURLString").thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openStream()).thenReturn(mockDependencyData.getDependencyDataInputStream());
        final DependencyDataProvider dependencyDataProvider = new URLDependencyDataProvider(new GsonDependencyReader(new Gson()), mockUrl);
        assertEquals("Read and provide proper dependencies",mockDependencyData.getExpectedSample(), dependencyDataProvider.get());
    }

    public void testFileDependencyDataProviderReturnReader() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments("MyURLString").thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openStream()).thenReturn(mockDependencyData.getDependencyDataInputStream());
        final DependencyReader dependencyReader = new GsonDependencyReader(new Gson());
        final URLDependencyDataProvider dependencyDataProvider = new URLDependencyDataProvider(dependencyReader, mockUrl);
        assertEquals("Provider must use given reader", dependencyReader, dependencyDataProvider.getDependencyReader());
    }

    public void testFileDependencyDataProviderNullOnException() throws Exception {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final DependencyReader mockReader = PowerMockito.mock(DependencyReader.class);
        PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments("MyURLString").thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openStream()).thenThrow(new IOException());
        final DependencyDataProvider dependencyDataProvider = new URLDependencyDataProvider(mockReader, mockUrl);
        assertNull("Provider must return null on exception", dependencyDataProvider.get());
    }
}
