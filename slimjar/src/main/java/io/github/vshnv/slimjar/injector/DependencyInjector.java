package io.github.vshnv.slimjar.injector;

import io.github.vshnv.slimjar.data.DependencyData;
import io.github.vshnv.slimjar.injector.loader.Injectable;

import java.io.IOException;

public interface DependencyInjector {
    void inject(Injectable injectable, DependencyData dependencyData) throws InjectionFailedException;
}
