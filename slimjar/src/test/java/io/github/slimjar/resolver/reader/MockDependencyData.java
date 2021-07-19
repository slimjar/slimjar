//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

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
