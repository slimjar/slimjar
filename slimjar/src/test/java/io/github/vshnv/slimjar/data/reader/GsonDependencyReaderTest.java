package io.github.vshnv.slimjar.data.reader;

import com.google.gson.Gson;
import io.github.vshnv.slimjar.data.Dependency;
import io.github.vshnv.slimjar.data.DependencyData;
import io.github.vshnv.slimjar.data.reader.DependencyReader;
import io.github.vshnv.slimjar.data.reader.GsonDependencyReader;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;

public class GsonDependencyReaderTest extends TestCase {
    private final Gson gson = new Gson();
    public void testLoadDependencies() {
        DependencyData data = new DependencyData(Arrays.asList(
                new Dependency("dep-a", "somerepo.tld/path/tojar", Collections.emptyList()),
                new Dependency("dep-b", "somerepo2.tld/path/tojar", Collections.emptyList()),
                new Dependency("dep-c", "somerepo3.tld/path/tojar", Collections.emptyList())
        ));
        String jsonData = gson.toJson(data);
        DependencyReader reader = new GsonDependencyReader(gson);
        DependencyData loadedData = reader.read(new ByteArrayInputStream(jsonData.getBytes()));
        assertEquals(loadedData, data);
    }
}