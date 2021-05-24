package io.github.slimjar.resolver.reader;

import com.google.gson.Gson;
import io.github.slimjar.reader.GsonDependencyDataProviderFactory;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;

public class DependencyDataProviderFactoryTest extends TestCase {

    public void testCreateFactory() throws MalformedURLException {
        final URL url = new URL("https://a.b.c");
        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(new Gson());
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("create must return a FileDependencyDataProvider", provider instanceof URLDependencyDataProvider);
    }

    public void testCreateFileDataProviderFactory() throws MalformedURLException {
        final URL url = new URL("https://a.b.c");
        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(new Gson());
        final DependencyDataProvider provider = dependencyDataProviderFactory.forFile(url);

        assertTrue("forFile must return a FileDependencyDataProvider", provider instanceof URLDependencyDataProvider);
    }

    public void testCreateModuleDataProviderFactory() throws MalformedURLException {
        final URL url = new URL("https://a.b.c");

        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(new Gson());
        final DependencyDataProvider provider = dependencyDataProviderFactory.forModule(url);

        assertTrue("forFile must return a FileDependencyDataProvider", provider instanceof ModuleDependencyDataProvider);
    }
}