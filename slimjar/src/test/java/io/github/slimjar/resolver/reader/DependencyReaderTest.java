package io.github.slimjar.resolver.reader;

import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;

public class DependencyReaderTest extends TestCase {

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
    public void testDependencyReaderParse() throws IOException, NoSuchAlgorithmException, ReflectiveOperationException, URISyntaxException {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final DependencyReader dependencyReader = new GsonDependencyReader(ReflectiveGsonFacadeFactory.create(DEFAULT_DOWNLOAD_DIRECTORY, CENTRAL_MIRRORS).createFacade());
        final InputStream inputStream = new ByteArrayInputStream(mockDependencyData.getSampleDependencyData().getBytes());
        final DependencyData dependencyData = dependencyReader.read(inputStream);
        assertEquals("Read dependency properly", dependencyData, mockDependencyData.getExpectedSample());
    }

}