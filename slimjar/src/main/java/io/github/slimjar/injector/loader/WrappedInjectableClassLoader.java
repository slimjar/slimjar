package io.github.slimjar.injector.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public final class WrappedInjectableClassLoader implements Injectable {
    private final URLClassLoader urlClassLoader;
    private final Method addURLMethod;

    public WrappedInjectableClassLoader(final URLClassLoader urlClassLoader) {
        Method methodDefer;
        this.urlClassLoader = urlClassLoader;
        try {
            methodDefer = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            methodDefer = null;
        }
        this.addURLMethod = methodDefer;
    }


    @Override
    public void inject(final URL url) throws InvocationTargetException, IllegalAccessException {
        addURLMethod.setAccessible(true);
        addURLMethod.invoke(urlClassLoader, url);
    }
}
