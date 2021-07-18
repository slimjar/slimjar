package io.github.slimjar.logging;

import java.util.Collections;

public final class LogDispatcher {
    private static final MediatingProcessLogger mediatingLogger = new MediatingProcessLogger(Collections.emptySet());

    private LogDispatcher() {
    }

    public static MediatingProcessLogger getMediatingLogger() {
        return mediatingLogger;
    }
}
