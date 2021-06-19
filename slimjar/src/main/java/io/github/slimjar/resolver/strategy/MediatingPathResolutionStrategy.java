package io.github.slimjar.resolver.strategy;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;

import java.util.Collection;

public final class MediatingPathResolutionStrategy implements PathResolutionStrategy {
    private final PathResolutionStrategy releaseStrategy;
    private final PathResolutionStrategy snapshotStrategy;

    public MediatingPathResolutionStrategy(final PathResolutionStrategy releaseStrategy, final PathResolutionStrategy snapshotStrategy) {
        this.releaseStrategy = releaseStrategy;
        this.snapshotStrategy = snapshotStrategy;
    }

    @Override
    public Collection<String> pathTo(final Repository repository, final Dependency dependency) {
        if (dependency.getSnapshotId() != null) {
            return snapshotStrategy.pathTo(repository, dependency);
        }
        return releaseStrategy.pathTo(repository, dependency);
    }
}
