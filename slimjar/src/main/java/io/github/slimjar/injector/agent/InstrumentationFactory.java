package io.github.slimjar.injector.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public interface InstrumentationFactory {
    Instrumentation create() throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException;
}
