package io.github.vshnv.slimjar.resolver;


import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.Repository;
import io.github.vshnv.slimjar.resolver.enquirer.RepositoryEnquirer;
import io.github.vshnv.slimjar.resolver.enquirer.RepositoryEnquirerFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public final class CachingDependencyResolver implements DependencyResolver {
    private final Collection<RepositoryEnquirer> repositories;
    private final Map<Dependency, URL> cachedResults = new HashMap<>();

    public CachingDependencyResolver(final Collection<Repository> repositories, final RepositoryEnquirerFactory enquirerFactory) {
        this.repositories = repositories.stream()
                .map(enquirerFactory::create)
                .collect(Collectors.toSet());
    }

    @Override
    public URL resolve(final Dependency dependency) {
        return Optional.ofNullable(cachedResults.get(dependency))
                .orElseGet(() -> attemptResolve(dependency));
    }

    private URL attemptResolve(final Dependency dependency) {
        final Optional<URL> resolvedUrl = repositories.stream().parallel()
                .map(enquirer -> enquirer.enquire(dependency))
                .filter(Objects::nonNull)
                .findFirst();
        final URL resolvedURL = resolvedUrl
                .orElseThrow(() -> new UnresolvedDependencyException(dependency));
        cachedResults.put(dependency, resolvedURL);
        return resolvedURL;
    }
}
