package io.github.slimjar.resolver.reader;

import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class DependencyReaderTest extends TestCase {

    public void testDependencyReaderParse() throws IOException, NoSuchAlgorithmException, ReflectiveOperationException, URISyntaxException {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final DependencyReader dependencyReader = new GsonDependencyReader(ReflectiveGsonFacadeFactory.create().createFacade());
        final InputStream inputStream = new ByteArrayInputStream(mockDependencyData.getSampleDependencyData().getBytes());
        final DependencyData dependencyData = dependencyReader.read(inputStream);
        assertEquals("Read dependency properly", dependencyData, mockDependencyData.getExpectedSample());
    }

}