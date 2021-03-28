package io.github.vshnv.slimjar.injector.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public final class WrappedInjectableClassLoader implements Injectable {
    private final URLClassLoader urlClassLoader;
    private final Method addURLMethod;

    public WrappedInjectableClassLoader(URLClassLoader urlClassLoader) {
        Method methodDefer;
        this.urlClassLoader = urlClassLoader;
        try {
            methodDefer = urlClassLoader.getClass().getMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            methodDefer = null;
        }
        this.addURLMethod = methodDefer;
    }


    @Override
    public void inject(URL url) throws InvocationTargetException, IllegalAccessException {
        addURLMethod.setAccessible(true);
        addURLMethod.invoke(urlClassLoader, url);
    }
}
