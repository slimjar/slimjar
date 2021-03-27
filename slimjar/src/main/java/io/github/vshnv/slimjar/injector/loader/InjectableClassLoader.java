package io.github.vshnv.slimjar.injector.loader;

import java.net.URL;
import java.net.URLClassLoader;

public final class InjectableClassLoader extends URLClassLoader implements Injectable {
    public InjectableClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public InjectableClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public void inject(URL url) {
        addURL(url);
    }
}
