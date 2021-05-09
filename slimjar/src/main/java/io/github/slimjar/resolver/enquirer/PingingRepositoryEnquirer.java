package io.github.slimjar.resolver.enquirer;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;
import io.github.slimjar.resolver.pinger.URLPinger;

import java.net.MalformedURLException;
import java.net.URL;

public final class PingingRepositoryEnquirer implements RepositoryEnquirer {
    private final Repository repository;
    private final PathResolutionStrategy urlCreationStrategy;
    private final URLPinger urlPinger;

    public PingingRepositoryEnquirer(final Repository repository, final PathResolutionStrategy urlCreationStratergy, URLPinger urlPinger) {
        this.repository = repository;
        this.urlCreationStrategy = urlCreationStratergy;
        this.urlPinger = urlPinger;
    }

    @Override
    public URL enquire(final Dependency dependency) {
        final String path = urlCreationStrategy.pathTo(repository, dependency);
        URL url;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            return null;
        }
        if (urlPinger.ping(url)) {
            return url;
        }
        return null;
    }
}
