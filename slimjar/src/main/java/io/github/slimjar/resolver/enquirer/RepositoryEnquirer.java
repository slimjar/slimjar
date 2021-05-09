package io.github.slimjar.resolver.enquirer;

import io.github.slimjar.resolver.data.Dependency;

import java.net.URL;

public interface RepositoryEnquirer {
    URL enquire(final Dependency dependency);
}
