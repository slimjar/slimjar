package io.github.slimjar.resolver.enquirer;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.resolver.strategy.MavenChecksumPathResolutionStrategy;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.net.URL;
import java.util.Collections;

@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, PingingRepositoryEnquirer.class, URLPinger.class})
public class RepositoryEnquirerTest extends TestCase {
    public void testPingingEnquirerProvideValidURL() throws Exception {
        final URL mockUrl = PowerMockito.mock(URL.class);

        final PathResolutionStrategy resolutionStrategy = PowerMockito.mock(PathResolutionStrategy.class);
        final URLPinger pinger = PowerMockito.mock(URLPinger.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockUrl);
        PowerMockito.doReturn(Collections.singleton("https://a.b.c/repo/dep.jar")).when(resolutionStrategy).pathTo(new Repository(mockUrl), new Dependency("a.b.c","d","", null, Collections.emptySet()));
        PowerMockito.doReturn(true).when(pinger).ping(mockUrl);

        final RepositoryEnquirer repositoryEnquirer = new PingingRepositoryEnquirer(new Repository(mockUrl), resolutionStrategy, new MavenChecksumPathResolutionStrategy("SHA-256", resolutionStrategy), pinger);
        assertNotNull("Valid repo & dep should return non-null URL", repositoryEnquirer.enquire(new Dependency("a.b.c","d","", null, Collections.emptySet())));
    }

    public void testPingingEnquirerProvideInvalidURL() throws Exception {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final PathResolutionStrategy resolutionStrategy = PowerMockito.mock(PathResolutionStrategy.class);
        final URLPinger pinger = PowerMockito.mock(URLPinger.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockUrl);
        PowerMockito.doReturn(Collections.singleton("https://a.b.c/repo/dep.jar")).when(resolutionStrategy).pathTo(null, null);
        PowerMockito.doReturn(false).when(pinger).ping(mockUrl);

        final RepositoryEnquirer repositoryEnquirer = new PingingRepositoryEnquirer(null, resolutionStrategy, new MavenChecksumPathResolutionStrategy("SHA-256", resolutionStrategy), pinger);
        assertNull("Invalid repo or dep should return null URL", repositoryEnquirer.enquire(null));
    }

    public void testPingingEnquirerProvideMalformedURL() throws Exception {
        final PathResolutionStrategy resolutionStrategy = PowerMockito.mock(PathResolutionStrategy.class);
        final URLPinger pinger = PowerMockito.mock(URLPinger.class);

        PowerMockito.doReturn(Collections.singleton("some_malformed_url")).when(resolutionStrategy).pathTo(null, null);

        final RepositoryEnquirer repositoryEnquirer = new PingingRepositoryEnquirer(null, resolutionStrategy, new MavenChecksumPathResolutionStrategy("SHA-256", resolutionStrategy), pinger);
        assertNull("Malformed URL should return null URL", repositoryEnquirer.enquire(null));
    }
}