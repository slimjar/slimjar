package io.github.slimjar.resolver.reader;

import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import junit.framework.TestCase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class DependencyDataProviderFactoryTest extends TestCase {

    public void testCreateFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");
        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create());
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("create must return a FileDependencyDataProvider", provider instanceof URLDependencyDataProvider);
    }

    public void testCreateFileDataProviderFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");
        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create());
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("forFile must return a FileDependencyDataProvider", provider instanceof URLDependencyDataProvider);
    }

    public void testCreateModuleDataProviderFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");

        final DependencyDataProviderFactory dependencyDataProviderFactory = new ExternalDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create());
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("forFile must return a FileDependencyDataProvider", provider instanceof ModuleDependencyDataProvider);
    }
}