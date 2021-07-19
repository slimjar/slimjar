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

package io.github.slimjar.resolver.enquirer;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.resolver.strategy.MavenChecksumPathResolutionStrategy;
import io.github.slimjar.resolver.strategy.MavenPomPathResolutionStrategy;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;
import io.github.slimjar.util.Repositories;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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

        final RepositoryEnquirer repositoryEnquirer = new PingingRepositoryEnquirer(new Repository(mockUrl), resolutionStrategy, new MavenChecksumPathResolutionStrategy("SHA-256", resolutionStrategy), new MavenPomPathResolutionStrategy(), pinger);
        assertNotNull("Valid repo & dep should return non-null URL", repositoryEnquirer.enquire(new Dependency("a.b.c","d","", null, Collections.emptySet())));
    }

    public void testPingingEnquirerProvideInvalidURL() throws Exception {
        final URL mockUrl = PowerMockito.mock(URL.class);
        final PathResolutionStrategy resolutionStrategy = PowerMockito.mock(PathResolutionStrategy.class);
        final URLPinger pinger = PowerMockito.mock(URLPinger.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(mockUrl);
        PowerMockito.doReturn(Collections.singleton("https://a.b.c/repo/dep.jar")).when(resolutionStrategy).pathTo(null, null);
        PowerMockito.doReturn(false).when(pinger).ping(mockUrl);

        final RepositoryEnquirer repositoryEnquirer = new PingingRepositoryEnquirer(null, resolutionStrategy, resolutionStrategy, resolutionStrategy, pinger);
        assertNull("Invalid repo or dep should return null URL", repositoryEnquirer.enquire(new Dependency("", "", "", null, Collections.emptySet())));
    }

    public void testPingingEnquirerProvideMalformedURL() throws Exception {
        final URL url = PowerMockito.mock(URL.class);
        final Repository repository = new Repository(url);
        final PathResolutionStrategy resolutionStrategy = PowerMockito.mock(PathResolutionStrategy.class);
        final URLPinger pinger = PowerMockito.mock(URLPinger.class);

        PowerMockito.doReturn(Collections.singleton("some_malformed_url")).when(resolutionStrategy).pathTo(repository, null);

        final RepositoryEnquirer repositoryEnquirer = new PingingRepositoryEnquirer(repository, resolutionStrategy, resolutionStrategy, resolutionStrategy, pinger);
        assertNull("Malformed URL should return null URL", repositoryEnquirer.enquire(new Dependency("", "", "", null, Collections.emptySet())));
    }
}