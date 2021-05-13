package io.github.slimjar.resolver.reader;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public final class MockDependencyData {
    private final String sampleDependencyData = "{\"mirrors\": [],\"repositories\": [{\"url\": \"https://repo.maven.apache.org/maven2/\"}],\"dependencies\": [{\"groupId\": \"com.google.code.gson\",\"artifactId\": \"gson\",\"version\": \"2.8.6\",\"transitive\": []}],\"relocations\": []}";

    private final DependencyData expectedSample = new DependencyData(
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

    public MockDependencyData() throws MalformedURLException {
    }

    public String getSampleDependencyData() {
        return sampleDependencyData;
    }

    public DependencyData getExpectedSample() {
        return expectedSample;
    }

    public InputStream getDependencyDataInputStream() {
        return new ByteArrayInputStream(getSampleDependencyData().getBytes());
    }
}
