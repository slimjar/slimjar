package io.github.slimjar.resolver.reader;

import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;

public class DependencyDataProviderFactoryTest extends TestCase {
    private static final Path DEFAULT_DOWNLOAD_DIRECTORY;
    private static final Collection<Repository> CENTRAL_MIRRORS;

    static {
        try {
            CENTRAL_MIRRORS = Collections.singleton(new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL)));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        final String userHome = System.getProperty("user.home");
        final String defaultPath = String.format("%s/.slimjar", userHome);
        DEFAULT_DOWNLOAD_DIRECTORY = new File(defaultPath).toPath();
    }


    public void testCreateFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");
        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS));
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("create must return a FileDependencyDataProvider", provider instanceof URLDependencyDataProvider);
    }

    public void testCreateFileDataProviderFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");
        final DependencyDataProviderFactory dependencyDataProviderFactory = new GsonDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS));
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("forFile must return a FileDependencyDataProvider", provider instanceof URLDependencyDataProvider);
    }

    public void testCreateModuleDataProviderFactory() throws IOException, URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException {
        final URL url = new URL("https://a.b.c");

        final DependencyDataProviderFactory dependencyDataProviderFactory = new ExternalDependencyDataProviderFactory(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS));
        final DependencyDataProvider provider = dependencyDataProviderFactory.create(url);

        assertTrue("forFile must return a FileDependencyDataProvider", provider instanceof ModuleDependencyDataProvider);
    }
}