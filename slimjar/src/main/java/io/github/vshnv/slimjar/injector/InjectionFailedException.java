package io.github.vshnv.slimjar.injector;

import io.github.vshnv.slimjar.data.Dependency;

public final class InjectionFailedException extends RuntimeException {
    private final Dependency dependency;
    public InjectionFailedException(Dependency dependency, Exception exception) {
        super("SlimJar failed to inject dependency: name -> " + dependency.getName(),exception);
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }
}
