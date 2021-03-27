package io.github.vshnv.slimjar.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class Dependency {
    private final String name;
    private final String url;
    private final Collection<Dependency> transitiveDependencies;

    public Dependency(String name, String url, Collection<Dependency> transitiveDependencies) {
        this.name = name;
        this.url = url;
        this.transitiveDependencies = new ArrayList<>(transitiveDependencies);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Collection<Dependency> getTransitiveDependencies() {
        return new ArrayList<>(transitiveDependencies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(name, that.name) &&
                url.equals(that.url) &&
                transitiveDependencies.equals(that.transitiveDependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, transitiveDependencies);
    }
}
