package io.github.vshnv.slimjar.resolver;


import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.Repository;

import java.net.URL;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CachingDependencyResolver implements DependencyResolver {
    private final Collection<RepositoryEnquirer> repositories;
    public CachingDependencyResolver(final Collection<Repository> repositories, final RepositoryEnquirerFactory enquirerFactory) {
        this.repositories = repositories.stream()
                .map(enquirerFactory::create)
                .collect(Collectors.toSet());
    }

    @Override
    public URL resolve(final Dependency dependency) {
        Optional<URL> resolvedUrl = repositories.stream().parallel()
                .map(enquirer -> enquirer.enquire(dependency))
                .filter(Objects::nonNull)
                .findFirst();
        return resolvedUrl
                .orElseThrow(() -> new UnresolvedDependencyException(dependency));
    }
}
