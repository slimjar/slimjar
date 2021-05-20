package io.github.slimjar.resolver.reader;

import com.google.gson.Gson;
import io.github.slimjar.reader.GsonDependencyReader;
import io.github.slimjar.resolver.data.DependencyData;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DependencyReaderTest extends TestCase {

    public void testDependencyReaderParse() throws IOException {
        final MockDependencyData mockDependencyData = new MockDependencyData();
        final DependencyReader dependencyReader = new GsonDependencyReader(new Gson());
        final InputStream inputStream = new ByteArrayInputStream(mockDependencyData.getSampleDependencyData().getBytes());
        final DependencyData dependencyData = dependencyReader.read(inputStream);
        assertEquals("Read dependency properly", dependencyData, mockDependencyData.getExpectedSample());
    }

}