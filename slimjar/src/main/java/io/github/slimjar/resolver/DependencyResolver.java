package io.github.slimjar.resolver;


import io.github.slimjar.resolver.data.Dependency;

import java.net.URL;

public interface DependencyResolver {
    URL resolve(final Dependency dependency);
}
