package io.github.vshnv.slimjar.injector;

import io.github.vshnv.slimjar.data.DependencyData;
import io.github.vshnv.slimjar.injector.loader.Injectable;


public interface DependencyInjector {
    void inject(final Injectable injectable, final DependencyData dependencyData) throws InjectionFailedException;
}
