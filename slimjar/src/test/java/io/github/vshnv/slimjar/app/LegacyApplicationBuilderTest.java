package io.github.vshnv.slimjar.app;

import io.github.vshnv.slimjar.data.DependencyData;
import io.github.vshnv.slimjar.injector.loader.InjectableClassLoader;
import junit.framework.TestCase;

import java.util.Collections;

public class LegacyApplicationBuilderTest extends TestCase {
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

    public void testAppendingApplicationLoader() {
        // todo: 'Assume' not working properly for some reason. Ill work it out later
        // Assume.assumeThat(System.getProperty("java.version"), CoreMatchers.startsWith("1.8"));
        if (!System.getProperty("java.version").startsWith("1.8")) return;
        Application app = AppendingApplication.builder()
                .withDependencies(
                        new DependencyData(
                                Collections.emptyList()
                        )
                )
                .buildAppendable();
        assertNotNull(app);
        assertTrue(app instanceof AppendingApplication);
    }
}
