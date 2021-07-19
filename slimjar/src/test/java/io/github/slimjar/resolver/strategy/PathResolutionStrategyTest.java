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

package io.github.slimjar.resolver.strategy;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class PathResolutionStrategyTest extends TestCase {

    public void testPathResolutionStrategyMaven() throws MalformedURLException {
        final String repoString = "https://repo.tld/";
        final Repository repository = new Repository(new URL(repoString));
        final Dependency dependency = new Dependency("a.b.c", "d", "1.0", null, Collections.emptySet());
        final PathResolutionStrategy pathResolutionStrategy = new MavenPathResolutionStrategy();
        final Collection<String> resolvedPath = pathResolutionStrategy.pathTo(repository, dependency);

        assertEquals("Maven Path Resolution (LOCAL)", new HashSet<>(resolvedPath), new HashSet<>(Collections.singleton("https://repo.tld/a/b/c/d/1.0/d-1.0.jar")));
    }

}