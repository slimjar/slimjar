package io.github.slimjar.resolver;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
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
        final DependencyResolver dependencyResolver = new CachingDependencyResolver(repositories, repositoryEnquirerFactory);
        final Dependency testDependency = new Dependency("a.b.c", "d", "1.0", null, Collections.emptyList());
        final Optional<ResolutionResult> url = dependencyResolver.resolve(testDependency);
        final String groupPath = testDependency.getGroupId().replace('.', '/');
        Assert.assertEquals(url.get().getDependencyURL().toString(), String.format("https://repo.tld/%s/%s/%s/%2$s-%3$s.jar", groupPath, testDependency.getArtifactId(), testDependency.getVersion()));
    }

    public void testCachingDependencyResolverCaching() throws MalformedURLException {
        final RepositoryEnquirerFactory repositoryEnquirerFactory = new DummyRepositoryEnquirerFactory();
        final Collection<Repository> repositories = Collections.singleton(new Repository(new URL("https://repo.tld/")));
        final DependencyResolver dependencyResolver = new CachingDependencyResolver(repositories, repositoryEnquirerFactory);
        final Dependency testDependency = new Dependency("a.b.c", "d", "1.0", null, Collections.emptyList());
        final URL url1 = dependencyResolver.resolve(testDependency).get().getDependencyURL();
        final URL url2 = dependencyResolver.resolve(testDependency).get().getDependencyURL();

        Assert.assertEquals("Impure resolution: Instance", url1, url2);
        Assert.assertSame("Impure resolution: Reference", url1, url2);
    }

}