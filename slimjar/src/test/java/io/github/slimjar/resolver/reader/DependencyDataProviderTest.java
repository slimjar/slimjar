package io.github.slimjar.resolver.reader;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, FileDependencyDataProvider.class})
public class DependencyDataProviderTest {
    @Test
    public void testFileDependencyDataProvider() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments("MyURLString").thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openStream()).thenReturn(mockDependencyData.getDependencyDataInputStream());
        final DependencyDataProvider dependencyDataProvider = new FileDependencyDataProvider(new GsonDependencyReader(new Gson()), mockUrl);
        assertEquals(mockDependencyData.getExpectedSample(), dependencyDataProvider.get());
    }
}