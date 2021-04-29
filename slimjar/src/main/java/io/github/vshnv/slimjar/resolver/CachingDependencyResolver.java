package io.github.vshnv.slimjar.resolver;

import io.github.vshnv.slimjar.data.Dependency;

import java.net.URL;
import java.util.Collection;

public final class CachingDependencyResolver implements DependencyResolver {
    private final Collection<Repository> repositories;

    public CachingDependencyResolver(final Collection<Repository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public URL resolve(Dependency dependency) {
        return null;
    }
}
