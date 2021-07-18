package io.github.slimjar.logging;

import java.util.HashSet;

public final class LogDispatcher {
    private static final MediatingProcessLogger mediatingLogger = new MediatingProcessLogger(new HashSet<>());

    private LogDispatcher() {
    }

    public static MediatingProcessLogger getMediatingLogger() {
        return mediatingLogger;
    }
}
