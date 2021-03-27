package io.github.vshnv.slimjar.data;

import java.util.Collection;
import java.util.Objects;

public final class DependencyData {
    private final Collection<Dependency> dependencies;

    public DependencyData(final Collection<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public Collection<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DependencyData data = (DependencyData) o;
        return dependencies.equals(data.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencies);
    }
}
