package io.github.slimjar.resolver.enquirer;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.pinger.HttpURLPinger;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.resolver.reader.MockDependencyData;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, PingingRepositoryEnquirer.class})
public class RepositoryEnquirerTest extends TestCase {
    public void testPingingEnquirerProvideValidURL() throws Exception {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final PathResolutionStrategy resolutionStrategy = PowerMockito.mock(PathResolutionStrategy.class);
        final URLPinger pinger = PowerMockito.mock(URLPinger.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockUrl);
        PowerMockito.doReturn("https://a.b.c/repo/dep.jar").when(resolutionStrategy).pathTo(null, null);
        PowerMockito.doReturn(true).when(pinger).ping(mockUrl);

        final RepositoryEnquirer repositoryEnquirer = new PingingRepositoryEnquirer(null, resolutionStrategy, pinger);
        assertNotNull("Valid repo & dep should return non-null URL", repositoryEnquirer.enquire(null));
    }

    public void testPingingEnquirerProvideInvalidURL() throws Exception {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final PathResolutionStrategy resolutionStrategy = PowerMockito.mock(PathResolutionStrategy.class);
        final URLPinger pinger = PowerMockito.mock(URLPinger.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockUrl);
        PowerMockito.doReturn("https://a.b.c/repo/dep.jar").when(resolutionStrategy).pathTo(null, null);
        PowerMockito.doReturn(false).when(pinger).ping(mockUrl);

        final RepositoryEnquirer repositoryEnquirer = new PingingRepositoryEnquirer(null, resolutionStrategy, pinger);
        assertNull("Invalid repo or dep should return null URL", repositoryEnquirer.enquire(null));
    }
}