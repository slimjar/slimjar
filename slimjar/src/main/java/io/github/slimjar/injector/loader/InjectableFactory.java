package io.github.slimjar.injector.loader;

import io.github.slimjar.app.builder.ApplicationBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.security.NoSuchAlgorithmException;

public final class InjectableFactory {
    private InjectableFactory() {
    }

    public static Injectable create() throws ReflectiveOperationException, NoSuchAlgorithmException, IOException, URISyntaxException {
        return create(InjectableFactory.class.getClassLoader());
    }

    public static Injectable create(final ClassLoader classLoader) throws URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException, IOException {
        final boolean isJigsawActive = isJigsawActive();
        Injectable injectable = null;

        if (isJigsawActive && classLoader instanceof URLClassLoader) {
            injectable = new WrappedInjectableClassLoader((URLClassLoader) ApplicationBuilder.class.getClassLoader());
        } else if (isUnsafeAvailable() && classLoader instanceof URLClassLoader) {
            try {
                injectable = UnsafeInjectable.create((URLClassLoader) classLoader);
            } catch (final Exception exception) {
                // ignored
            }
        }

        if (injectable == null) {
            injectable = InstrumentationInjectable.create();
        }
        return injectable;
    }

    private static boolean isJigsawActive() {
        try {
            Class.forName("java.lang.Module");
        } catch (final ClassNotFoundException e) {
            return true;
        }
        return false;
    }

    private static boolean isUnsafeAvailable() {
        try {
            Class.forName("sun.misc.Unsafe");
        } catch (final ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
