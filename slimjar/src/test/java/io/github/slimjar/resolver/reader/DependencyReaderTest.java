package io.github.slimjar.resolver.reader;

import com.google.gson.Gson;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class DependencyReaderTest extends TestCase {
    private static final String SAMPLE_DEPENDENCY_DATA = "{\"mirrors\": [],\"repositories\": [{\"url\": \"https://repo.maven.apache.org/maven2/\"}],\"dependencies\": [{\"groupId\": \"com.google.code.gson\",\"artifactId\": \"gson\",\"version\": \"2.8.6\",\"transitive\": []}],\"relocations\": []}";
    private static final DependencyData EXPECTED_SAMPLE;

    static {
        DependencyData EXPECTED_SAMPLE_DEFER;
        try {
            EXPECTED_SAMPLE_DEFER = new DependencyData(
                    Collections.emptySet(),
                    Collections.singleton(new Repository(
                            new URL("https://repo.maven.apache.org/maven2/")
                    )),
                    Collections.singleton(new Dependency(
                            "com.google.code.gson",
                            "gson",
                            "2.8.6",
                            null,
                            Collections.emptyList()
                    )),
                    Collections.emptyList()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            EXPECTED_SAMPLE_DEFER = null;
        }
        EXPECTED_SAMPLE = EXPECTED_SAMPLE_DEFER;
    }

    public void testDependencyReaderParse() throws IOException {
        final DependencyReader dependencyReader = new GsonDependencyReader(new Gson());
        final InputStream inputStream = new ByteArrayInputStream(SAMPLE_DEPENDENCY_DATA.getBytes());
        final DependencyData dependencyData = dependencyReader.read(inputStream);
        assertEquals("Read dependency properly", dependencyData, EXPECTED_SAMPLE);
    }

}