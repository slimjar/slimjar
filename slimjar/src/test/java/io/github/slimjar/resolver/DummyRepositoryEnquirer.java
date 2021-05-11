package io.github.slimjar.resolver;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirer;

import java.net.MalformedURLException;
import java.net.URL;

public final class DummyRepositoryEnquirer implements RepositoryEnquirer {
    @Override
    public URL enquire(final Dependency dependency) {
        final String groupPath = dependency.getGroupId().replace('.', '/');
        try {
            return new URL(String.format("https://repo.tld/%s/%s/%s/%2$s-%3$s.jar", groupPath, dependency.getArtifactId(), dependency.getVersion()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
