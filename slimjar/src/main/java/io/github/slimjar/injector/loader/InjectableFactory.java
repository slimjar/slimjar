package io.github.slimjar.injector.loader;

import io.github.slimjar.app.builder.ApplicationBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.security.NoSuchAlgorithmException;

public final class InjectableFactory {
    private InjectableFactory() {
    }

    public static Injectable create(final ClassLoader classLoader) throws URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException, IOException {
        final boolean legacy = isOnLegacyJVM();
        Injectable injectable = null;

        if (legacy && classLoader instanceof URLClassLoader) {
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

    private static boolean isOnLegacyJVM() {
        final String version = System.getProperty("java.version");
        final String[] parts = version.split("\\.");

        final int jvmLevel;
        switch (parts.length) {
            case 0:
                jvmLevel = 16; // Assume highest if not found.
                break;
            case 1:
                jvmLevel = Integer.parseInt(parts[0]);
                break;
            default:
                jvmLevel = Integer.parseInt(parts[1]);
                break;
        }
        return jvmLevel < 9;
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
