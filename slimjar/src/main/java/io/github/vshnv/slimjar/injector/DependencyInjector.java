package io.github.vshnv.slimjar.injector;

import io.github.vshnv.slimjar.injector.loader.Injectable;
import io.github.vshnv.slimjar.resolver.data.DependencyData;


public interface DependencyInjector {
    void inject(final Injectable injectable, final DependencyData dependencyData) throws InjectionFailedException;
}
