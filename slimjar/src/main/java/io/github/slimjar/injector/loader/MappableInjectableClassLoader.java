package io.github.slimjar.injector.loader;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class MappableInjectableClassLoader extends IsolatedInjectableClassLoader {
    private final Map<String, Class<?>> mappings;

    public MappableInjectableClassLoader(final URL[] urls, final Collection<Class<?>> delegates, final Map<String, Class<?>> mappings) {
        super(urls, delegates);
        this.mappings = new HashMap<>(mappings);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> mappedClazz = mappings.get(name);
        if (mappedClazz != null) {
            return mappedClazz;
        }
        return super.loadClass(name, resolve);
    }
}
