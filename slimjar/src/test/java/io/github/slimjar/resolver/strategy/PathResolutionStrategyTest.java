package io.github.slimjar.resolver.strategy;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class PathResolutionStrategyTest extends TestCase {

    public void testPathResolutionStrategyMaven() throws MalformedURLException {
        final String repoString = "https://repo.tld/";
        final Repository repository = new Repository(new URL(repoString));
        final Dependency dependency = new Dependency("a.b.c", "d", "1.0", null, Collections.emptySet());
        final PathResolutionStrategy pathResolutionStrategy = new MavenPathResolutionStrategy();
        final Collection<String> resolvedPath = pathResolutionStrategy.pathTo(repository, dependency);

        assertEquals("Maven Path Resolution (LOCAL)", new HashSet<>(resolvedPath), new HashSet<>(Collections.singleton("https://repo.tld/a/b/c/d/1.0/d-1.0.jar")));
    }

}