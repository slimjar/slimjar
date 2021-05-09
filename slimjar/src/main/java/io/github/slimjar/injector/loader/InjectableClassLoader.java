package io.github.slimjar.injector.loader;

import java.net.URL;
import java.net.URLClassLoader;

public abstract class InjectableClassLoader extends URLClassLoader implements Injectable {
    static {
        registerAsParallelCapable();
    }

    public InjectableClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }


    @Override
    public void inject(final URL url) {
        addURL(url);
    }

}
