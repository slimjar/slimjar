package io.github.vshnv.slimjar.resolver.reader;

import com.google.gson.Gson;
import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.DependencyData;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;

public class GsonDependencyReaderTest extends TestCase {
    private final Gson gson = new Gson();
    public void testLoadDependencies() {
        DependencyData data = new DependencyData(Collections.emptySet(), Collections.emptySet(), Arrays.asList(
                new Dependency("dep-a", "somerepo.tld/path/tojar", "", "", Collections.emptyList()),
                new Dependency("dep-b", "somerepo2.tld/path/tojar", "", "", Collections.emptyList()),
                new Dependency("dep-c", "somerepo3.tld/path/tojar", "", "", Collections.emptyList())
        ),  Collections.emptySet());
        String jsonData = gson.toJson(data);
        DependencyReader reader = new GsonDependencyReader(gson);
        DependencyData loadedData = reader.read(new ByteArrayInputStream(jsonData.getBytes()));
        assertEquals(loadedData, data);
    }
}