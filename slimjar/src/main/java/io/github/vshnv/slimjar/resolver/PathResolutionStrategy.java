package io.github.vshnv.slimjar.resolver;

import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.Repository;

public interface PathResolutionStrategy {
    String pathTo(final Repository repository, final Dependency dependency);
}
