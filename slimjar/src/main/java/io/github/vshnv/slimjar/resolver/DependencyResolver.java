package io.github.vshnv.slimjar.resolver;

import io.github.vshnv.slimjar.data.Dependency;

import java.net.URL;

public interface DependencyResolver {
    URL resolve(final Dependency dependency);
}
