package io.github.slimjar.resolver.enquirer;

import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;

public class SimpleRepositoryEnquirerFactory implements RepositoryEnquirerFactory {
    private final PathResolutionStrategy pathResolutionStrategy;
    private final URLPinger urlPinger;

    public SimpleRepositoryEnquirerFactory(final PathResolutionStrategy pathResolutionStrategy, final URLPinger urlPinger) {
        this.pathResolutionStrategy = pathResolutionStrategy;
        this.urlPinger = urlPinger;
    }

    public PathResolutionStrategy getPathResolutionStrategy() {
        return pathResolutionStrategy;
    }

    @Override
    public RepositoryEnquirer create(final Repository repository) {
        return new PingingRepositoryEnquirer(repository, pathResolutionStrategy, urlPinger);
    }
}
