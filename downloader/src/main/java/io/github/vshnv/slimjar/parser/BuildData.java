package io.github.vshnv.slimjar.parser;

import io.github.vshnv.slimjar.data.Dependency;
import io.github.vshnv.slimjar.data.Repository;

import java.util.ArrayList;
import java.util.List;

public class BuildData {
    private final List<Dependency> dependencies;
    private final List<Repository> repositories;

    public BuildData(List<Dependency> dependencies, List<Repository> repositories) {
        this.dependencies = dependencies;
        this.repositories = repositories;
    }

    public List<Dependency> getDependencies() {
        return new ArrayList<>(dependencies);
    }

    public List<Repository> getRepositories() {
        return new ArrayList<>(repositories);
    }
}
