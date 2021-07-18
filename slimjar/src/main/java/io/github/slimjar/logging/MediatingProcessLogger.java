package io.github.slimjar.logging;

import java.util.Collection;

public final class MediatingProcessLogger implements ProcessLogger {
    private final Collection<ProcessLogger> loggers;

    public MediatingProcessLogger(final Collection<ProcessLogger> loggers) {
        this.loggers = loggers;
    }

    @Override
    public void log(final String message, final Object... args) {
        for (final ProcessLogger logger : loggers) {
            logger.log(message, args);
        }
    }

    @Override
    public void debug(final String message, final Object... args) {
        for (final ProcessLogger logger : loggers) {
            logger.debug(message, args);
        }
    }

    public void addLogger(final ProcessLogger logger) {
        this.loggers.add(logger);
    }

    public void removeLogger(final ProcessLogger logger) {
        this.loggers.remove(logger);
    }

    public void clearLoggers() {
        this.loggers.clear();
    }
}
