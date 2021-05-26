package io.github.slimjar.relocation.facade;

import io.github.slimjar.injector.loader.InjectableClassLoader;
import io.github.slimjar.relocation.RelocationRule;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectiveJarRelocatorFacade implements JarRelocatorFacade {
    private final Object relocator;
    private final Method relocatorRunMethod;

    ReflectiveJarRelocatorFacade(final Object relocator, final Method relocatorRunMethod) {
        this.relocator = relocator;
        this.relocatorRunMethod = relocatorRunMethod;
    }

    @Override
    public void run() throws InvocationTargetException, IllegalAccessException {
        relocatorRunMethod.invoke(relocator);
    }
}
