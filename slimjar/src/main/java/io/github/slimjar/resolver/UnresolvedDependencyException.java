package io.github.slimjar.resolver;

import io.github.slimjar.resolver.data.Dependency;

public final class UnresolvedDependencyException extends RuntimeException {
    private final Dependency dependency;
    public UnresolvedDependencyException(final Dependency dependency) {
        super("Could not resolve dependency : " + dependency);
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }

    @Override
    public String toString() {
        return "UnresolvedDependencyException{" +
                "dependency=" + dependency +
                '}';
    }
}
