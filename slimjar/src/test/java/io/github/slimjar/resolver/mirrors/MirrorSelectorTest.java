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

package io.github.slimjar.resolver.mirrors;

import io.github.slimjar.resolver.data.Mirror;
import io.github.slimjar.resolver.data.Repository;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MirrorSelectorTest extends TestCase {
    public void testSelectorForceMirrorCentral() throws MalformedURLException {
        final Repository central = new Repository(new URL(SimpleMirrorSelector.CENTRAL_URL));
        final Repository centralMirror = new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL));
        final Collection<Repository> original = Collections.singleton(central);
        final MirrorSelector mirrorSelector = new SimpleMirrorSelector(Collections.singleton(centralMirror));
        final Collection<Repository> selected = mirrorSelector.select(original, Collections.emptyList());
        assertFalse("Selection should remove central", selected.contains(central));
        assertTrue("Selection should contain central mirror", selected.contains(new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL))));
    }

    public void testSelectorForceAltMirrorCentral() throws MalformedURLException {
        final Repository central = new Repository(new URL(SimpleMirrorSelector.CENTRAL_URL));
        final Repository centralMirror = new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL));

        final Collection<Repository> original = Collections.singleton(central);
        final MirrorSelector mirrorSelector = new SimpleMirrorSelector(Collections.singleton(centralMirror));

        final Collection<Repository> selected = mirrorSelector.select(original, Collections.emptyList());
        assertFalse("Selection should remove central", selected.contains(central));
        assertTrue("Selection should contain central mirror", selected.contains(new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL))));
    }

    public void testSelectorReplace() throws MalformedURLException {
        final Repository central = new Repository(new URL(SimpleMirrorSelector.CENTRAL_URL));
        final Collection<Repository> centralMirrors = Collections.singleton(central);
        final Repository originalRepo = new Repository(new URL("https://a.b.c"));
        final Repository mirroredRepo = new Repository(new URL("https://d.e.f"));
        final Mirror mirror = new Mirror(mirroredRepo.getUrl(), originalRepo.getUrl());
        final Collection<Repository> original = Collections.singleton(originalRepo);
        final Collection<Mirror> mirrors = Collections.singleton(mirror);
        final MirrorSelector mirrorSelector = new SimpleMirrorSelector(centralMirrors);


        final Collection<Repository> selected = mirrorSelector.select(original, mirrors);
        assertFalse("Selection should remove original", selected.contains(originalRepo));
        assertTrue("Selection should contain mirror", selected.contains(mirroredRepo));
    }
}