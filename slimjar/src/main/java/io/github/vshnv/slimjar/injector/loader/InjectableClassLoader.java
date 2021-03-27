package io.github.vshnv.slimjar.injector.loader;

import java.net.URL;
import java.net.URLClassLoader;

public final class InjectableClassLoader extends URLClassLoader implements Injectable {
    public InjectableClassLoader(final ClassLoader parent) {
        super(new URL[0], parent);
    }

    public InjectableClassLoader(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public void inject(final URL url) {
        addURL(url);
    }
}
