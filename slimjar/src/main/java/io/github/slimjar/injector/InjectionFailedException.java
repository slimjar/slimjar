package io.github.slimjar.injector;

import io.github.slimjar.resolver.data.Dependency;

public final class InjectionFailedException extends RuntimeException {
    private final Dependency dependency;
    public InjectionFailedException(Dependency dependency, Exception exception) {
        super("SlimJar failed to inject dependency: name -> " + dependency,exception);
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }
}
