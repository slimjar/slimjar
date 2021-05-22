package io.github.slimjar.app.external;

import java.util.Collection;
import java.util.Map;

public interface ExposedClassHelper {
    Collection<Class<?>> fetchExposedClasses();
    Map<String, Class<?>> fetchRemappedRelocations();
}
