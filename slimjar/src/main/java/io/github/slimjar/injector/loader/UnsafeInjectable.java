package io.github.slimjar.injector.loader;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayDeque;
import java.util.ArrayList;

public final class UnsafeInjectable implements Injectable {
    private final ArrayDeque<URL> unopenedURLs;
    private final ArrayList<URL> pathURLs;

    public UnsafeInjectable(final ArrayDeque<URL> unopenedURLs, final ArrayList<URL> pathURLs) {
        this.unopenedURLs = unopenedURLs;
        this.pathURLs = pathURLs;
    }

    @Override
    public void inject(final URL url) {
        unopenedURLs.addLast(url);
        pathURLs.add(url);
    }

    public static Injectable create(final URLClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
        final Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        final  Unsafe unsafe = (Unsafe) field.get(null);
        final Object ucp = fetchField(unsafe, URLClassLoader.class, classLoader, "ucp");
        final ArrayDeque<URL> unopenedURLs = (ArrayDeque<URL>) fetchField(unsafe, ucp, "unopenedUrls");
        final ArrayList<URL> pathURLs = (ArrayList<URL>) fetchField(unsafe, ucp, "path");
        return new UnsafeInjectable(unopenedURLs, pathURLs);
    }



    private static Object fetchField(final Unsafe unsafe, final Object object, final String name) throws NoSuchFieldException {
        return fetchField(unsafe, object.getClass(), object, name);
    }

    private static Object fetchField(final Unsafe unsafe, final Class<?> clazz, final Object object, final String name) throws NoSuchFieldException {
        final Field field = clazz.getDeclaredField(name);
        final long offset = unsafe.objectFieldOffset(field);
        return unsafe.getObject(object, offset);
    }
}
