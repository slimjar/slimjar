package io.github.slimjar.injector;

import io.github.slimjar.injector.loader.Injectable;
import io.github.slimjar.resolver.data.Dependency;

import java.util.Collection;


public interface DependencyInjector {
    void inject(final Injectable injectable, final Collection<Dependency> dependencies) throws InjectionFailedException;
}
