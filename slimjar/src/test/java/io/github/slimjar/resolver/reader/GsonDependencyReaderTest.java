package io.github.slimjar.resolver.reader;

import com.google.gson.Gson;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.DependencyData;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class GsonDependencyReaderTest extends TestCase {
    /*public void testLoadDependencies() throws IOException {
        DependencyData data = new DependencyData(Collections.emptySet(), Collections.emptySet(), Arrays.asList(
                new Dependency("dep-a", "somerepo.tld/path/tojar", "", "", Collections.emptyList()),
                new Dependency("dep-b", "somerepo2.tld/path/tojar", "", "", Collections.emptyList()),
                new Dependency("dep-c", "somerepo3.tld/path/tojar", "", "", Collections.emptyList())
        ),  Collections.emptySet());
        String jsonData = gson.toJson(data);
        DependencyReader reader = new GsonDependencyReader(gson);
        DependencyData loadedData = reader.read(new ByteArrayInputStream(jsonData.getBytes()));
        assertEquals(loadedData, data);
    }*/
}