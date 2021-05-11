package io.github.slimjar.resolver;


import io.github.slimjar.resolver.data.Dependency;

import java.net.URL;
import java.util.Optional;

public interface DependencyResolver {
    Optional<URL> resolve(final Dependency dependency);
}
