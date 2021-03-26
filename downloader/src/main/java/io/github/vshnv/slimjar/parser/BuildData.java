package io.github.vshnv.slimjar.parser;

import io.github.vshnv.slimjar.data.Dependency;
import io.github.vshnv.slimjar.data.Repository;

import java.util.*;

public final class BuildData {
    private final Set<Dependency> dependencies;
    private final Set<Repository> repositories;

    public BuildData(Collection<Dependency> dependencies, Collection<Repository> repositories) {
        this.dependencies = new HashSet<>(dependencies);
        this.repositories = new HashSet<>(repositories);
    }

    public Set<Dependency> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    public Set<Repository> getRepositories() {
        return Collections.unmodifiableSet(repositories);
    }
}
