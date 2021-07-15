package io.github.slimjar.resolver.reader.provider;

import com.google.gson.Gson;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.reader.*;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, URLDependencyDataProvider.class})
public class URLDependencyDataProviderTest extends TestCase {
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

    public void testFileDependencyDataProvider() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments("MyURLString").thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openStream()).thenReturn(mockDependencyData.getDependencyDataInputStream());
        final DependencyDataProvider dependencyDataProvider = new URLDependencyDataProvider(new GsonDependencyReader(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS).createFacade()), mockUrl);
        assertEquals("Read and provide proper dependencies",mockDependencyData.getExpectedSample(), dependencyDataProvider.get());
    }

    public void testFileDependencyDataProviderReturnReader() throws Exception {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments("MyURLString").thenReturn(mockUrl);
        PowerMockito.when(mockUrl.openStream()).thenReturn(mockDependencyData.getDependencyDataInputStream());
        final DependencyReader dependencyReader = new GsonDependencyReader(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS).createFacade());
        final URLDependencyDataProvider dependencyDataProvider = new URLDependencyDataProvider(dependencyReader, mockUrl);
        assertEquals("Provider must use given reader", dependencyReader, dependencyDataProvider.getDependencyReader());
    }

    public void testFileDependencyDataProviderOnUrlException() throws Exception {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final DependencyReader mockReader = PowerMockito.mock(DependencyReader.class);
        PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments("MyURLString").thenReturn(mockUrl);
        final Exception expectedException = new IOException();
        PowerMockito.when(mockUrl.openStream()).thenThrow(expectedException);
        Exception exception = null;
        try {
            new URLDependencyDataProvider(mockReader, mockUrl).get();
        } catch (final Exception ex) {
            exception = ex;
        }
        assertSame("Provider must elevate exception on url issue", expectedException, exception);

    }
}
