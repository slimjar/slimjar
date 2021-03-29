package io.github.vshnv.slimjar.injector.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public final class InjectableClassLoader extends URLClassLoader implements Injectable {
    private final String entryPointClass;

    public InjectableClassLoader(final String entryPointClass) {
        super(new URL[0]);
        this.entryPointClass = entryPointClass;
    }

    @Override
    public void inject(final URL url) {
        addURL(url);
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        if (name.equals(entryPointClass)) {
            Class<?> clazz = findClass(name);
            return clazz;
        }
        return super.loadClass(name);
    }

    @Override
    public Class<?> findClass(final String name) throws ClassNotFoundException {
        Class<?> loaded = findLoadedClass(name);
        if (loaded != null) return loaded;

        byte[] b = loadClassFromFile(name);
        if (b != null) return defineClass(name, b, 0, b.length);

        return super.findClass(name);
    }

    private byte[] loadClassFromFile(final String fileName)  {
        InputStream inputStream = getParent().getResourceAsStream(
                fileName.replace('.', File.separatorChar) + ".class");
        if (inputStream == null) {
            return null;
        }
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }
}
