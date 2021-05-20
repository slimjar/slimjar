package io.github.slimjar.util;

import io.github.slimjar.app.ApplicationFactory;

import java.net.URL;

public final class Modules {
    private Modules() {
    }

    public static URL findModule(final String moduleName) {
        final ClassLoader classLoader = ApplicationFactory.class.getClassLoader();
        return classLoader.getResource(moduleName + ".isolated-jar");
    }
}
