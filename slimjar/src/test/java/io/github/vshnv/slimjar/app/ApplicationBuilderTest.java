package io.github.vshnv.slimjar.app;

import io.github.vshnv.slimjar.data.Dependency;
import io.github.vshnv.slimjar.data.DependencyData;
import io.github.vshnv.slimjar.injector.loader.InjectableClassLoader;
import junit.framework.TestCase;

import java.util.Collections;

public class ApplicationBuilderTest extends TestCase {
    public void testInjectingApplicationLoader() throws ReflectiveOperationException {
        Application app = AppendingApplication.builder()
                .withDependencies(
                        new DependencyData(
                                Collections.emptyList()
                        )
                )
                .buildInjectable("io.github.vshnv.slimjar.app.DummyApplication");
        assertNotNull(app);
        assertEquals(app.getClass().getClassLoader().getClass(), InjectableClassLoader.class);
    }
}
