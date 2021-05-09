package io.github.slimjar.resolver.strategy;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;

public interface PathResolutionStrategy {
    String pathTo(final Repository repository, final Dependency dependency);
}
