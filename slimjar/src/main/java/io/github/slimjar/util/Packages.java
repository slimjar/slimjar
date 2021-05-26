package io.github.slimjar.util;

public final class Packages {
    private Packages() {

    }

    /**
     * This exists to bypass relocation so that original classes can be mapped back
     * @param input package name separated by #
     * @return proper package name
     */
    public static String fix(final String input) {
        return input.replace('#', '.');
    }
}
