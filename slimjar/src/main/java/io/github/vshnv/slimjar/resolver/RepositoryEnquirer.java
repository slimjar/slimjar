package io.github.vshnv.slimjar.resolver;

import io.github.vshnv.slimjar.resolver.data.Dependency;

import java.net.URL;

public interface RepositoryEnquirer {
    URL enquire(final Dependency dependency);
}
