package io.github.vshnv.slimjar.injector;

import io.github.vshnv.slimjar.injector.loader.Injectable;
import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.DependencyData;

import java.util.Collection;


public interface DependencyInjector {
    void inject(final Injectable injectable, final Collection<Dependency> dependencies) throws InjectionFailedException;
}
