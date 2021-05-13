package io.github.slimjar.resolver.reader.provider;

import com.google.gson.Gson;
import io.github.slimjar.resolver.reader.*;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.net.URL;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, FileDependencyDataProvider.class})
public class FileDependencyDataProviderTest extends TestCase {
    public void testFileDependencyDataProvider() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments("MyURLString").thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openStream()).thenReturn(mockDependencyData.getDependencyDataInputStream());
        final DependencyDataProvider dependencyDataProvider = new FileDependencyDataProvider(new GsonDependencyReader(new Gson()), mockUrl);
        assertEquals("Read and provide proper dependencies",mockDependencyData.getExpectedSample(), dependencyDataProvider.get());
    }
}
