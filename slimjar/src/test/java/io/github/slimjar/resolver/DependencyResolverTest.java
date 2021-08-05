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

package io.github.slimjar.resolver;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import io.github.slimjar.resolver.pinger.URLPinger;
import junit.framework.TestCase;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class DependencyResolverTest extends TestCase {

    public void testCachingDependencyResolverResolution() throws MalformedURLException {
        final RepositoryEnquirerFactory repositoryEnquirerFactory = new DummyRepositoryEnquirerFactory();
        final Collection<Repository> repositories = Collections.singleton(new Repository(new URL("https://repo.tld/")));
        final DependencyResolver dependencyResolver = new CachingDependencyResolver(new URLPinger() {
            @Override
            public boolean ping(URL url) {
                return true;
            }

            @Override
            public boolean isSupported(URL url) {
                return true;
            }
        }, repositories, repositoryEnquirerFactory, Collections.emptyMap());
        final Dependency testDependency = new Dependency("a.b.c", "d", "1.0", null, Collections.emptyList());
        final Optional<ResolutionResult> url = dependencyResolver.resolve(testDependency);
        final String groupPath = testDependency.getGroupId().replace('.', '/');
        Assert.assertEquals(url.get().getDependencyURL().toString(), String.format("https://repo.tld/%s/%s/%s/%2$s-%3$s.jar", groupPath, testDependency.getArtifactId(), testDependency.getVersion()));
    }

    public void testCachingDependencyResolverCaching() throws MalformedURLException {
        final RepositoryEnquirerFactory repositoryEnquirerFactory = new DummyRepositoryEnquirerFactory();
        final Collection<Repository> repositories = Collections.singleton(new Repository(new URL("https://repo.tld/")));
        final DependencyResolver dependencyResolver = new CachingDependencyResolver(new URLPinger() {
            @Override
            public boolean ping(URL url) {
                return true;
            }

            @Override
            public boolean isSupported(URL url) {
                return true;
            }
        }, repositories, repositoryEnquirerFactory, Collections.emptyMap());
        final Dependency testDependency = new Dependency("a.b.c", "d", "1.0", null, Collections.emptyList());
        final URL url1 = dependencyResolver.resolve(testDependency).get().getDependencyURL();
        final URL url2 = dependencyResolver.resolve(testDependency).get().getDependencyURL();

        Assert.assertEquals("Impure resolution: Instance", url1, url2);
    }

}