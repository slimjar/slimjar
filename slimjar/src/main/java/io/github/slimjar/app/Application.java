package io.github.slimjar.app;

import java.util.HashMap;
import java.util.Map;

public abstract class Application {
    private final Map<Class<?>, Object> facadeMapping = new HashMap<>();

    @SuppressWarnings("unchecked")
    protected final <T, U extends T> T registerFacade(Class<T> clazz, U t) {
        Object obj = facadeMapping.put(clazz, t);
        if (obj != null && !(clazz.isAssignableFrom(obj.getClass()))) {
            throw new IllegalStateException("Previous facade value did not conform to type restriction!");
        }
        return (T) obj;
    }

    @SuppressWarnings("unchecked")
    public final <T> T getFacade(Class<T> clazz) {
        Object obj = facadeMapping.get(clazz);
        if (obj == null) return null;
        if (!(clazz.isAssignableFrom(obj.getClass()))) {
            throw new IllegalStateException("Current facade value does not conform to type restriction!");
        }
        return (T) obj;
    }

    public boolean start() { return false; }

    public boolean stop() { return false; }
}
