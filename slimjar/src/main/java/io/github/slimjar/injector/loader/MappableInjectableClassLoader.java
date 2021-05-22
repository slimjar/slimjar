package io.github.slimjar.injector.loader;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class MappableInjectableClassLoader extends InjectableClassLoader {
    private final Map<String, Class<?>> mappings;

    public MappableInjectableClassLoader(final URL[] urls, final Map<String, Class<?>> mappings) {
        super(urls, getSystemClassLoader().getParent());
        this.mappings = new HashMap<>(mappings);

    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        System.out.println("\nLOADING CLASS");

        System.out.println(name);
        final Class<?> mappedClazz = mappings.get(name);
        if (mappedClazz != null) {
            System.out.println("FOUND");
            return mappedClazz;
        }
        System.out.println("NOT FOUND\n");
        return super.loadClass(name, resolve);
    }
}
