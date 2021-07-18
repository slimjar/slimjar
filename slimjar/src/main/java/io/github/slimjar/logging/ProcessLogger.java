package io.github.slimjar.logging;

@FunctionalInterface
public interface ProcessLogger {
    void log(final String message, final Object... args);
    default void debug(final String message, final Object... args) { }
}
