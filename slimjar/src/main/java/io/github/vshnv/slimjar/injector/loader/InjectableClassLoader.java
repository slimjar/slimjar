package io.github.vshnv.slimjar.injector.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
