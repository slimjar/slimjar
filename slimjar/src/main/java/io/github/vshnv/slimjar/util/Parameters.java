package io.github.vshnv.slimjar.util;

public final class Parameters {
    private Parameters() { }

    public static Class<?>[] typesFrom(final Object... args) {
        Class<?>[] result = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            Object current = args[i];
            if (current == null) continue;
            result[i] = current.getClass();
        }
        return result;
    }
}
