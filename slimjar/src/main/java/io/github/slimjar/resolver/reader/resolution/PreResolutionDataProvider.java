package io.github.slimjar.resolver.reader.resolution;

import io.github.slimjar.resolver.ResolutionResult;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@FunctionalInterface
public interface PreResolutionDataProvider {
    Map<String, ResolutionResult> get() throws IOException, ReflectiveOperationException;
}
