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
        final MirrorSelector mirrorSelector = new SimpleMirrorSelector();
        final Repository central = new Repository(new URL(SimpleMirrorSelector.CENTRAL_URL));
        final Collection<Repository> original = Collections.singleton(central);
        final Collection<Repository> selected = mirrorSelector.select(original, Collections.emptyList());
        assertFalse("Selection should remove central", selected.contains(central));
        assertTrue("Selection should contain central mirror", selected.contains(new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL))));
    }

    public void testSelectorForceAltMirrorCentral() throws MalformedURLException {
        final MirrorSelector mirrorSelector = new SimpleMirrorSelector();
        final Repository central = new Repository(new URL(SimpleMirrorSelector.ALT_CENTRAL_URL));
        final Collection<Repository> original = Collections.singleton(central);
        final Collection<Repository> selected = mirrorSelector.select(original, Collections.emptyList());
        assertFalse("Selection should remove central", selected.contains(central));
        assertTrue("Selection should contain central mirror", selected.contains(new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL))));
    }

    public void testSelectorReplace() throws MalformedURLException {
        final MirrorSelector mirrorSelector = new SimpleMirrorSelector();
        final Repository originalRepo = new Repository(new URL("https://a.b.c"));
        final Repository mirroredRepo = new Repository(new URL("https://d.e.f"));
        final Mirror mirror = new Mirror(mirroredRepo.getUrl(), originalRepo.getUrl());
        final Collection<Repository> original = Collections.singleton(originalRepo);
        final Collection<Mirror> mirrors = Collections.singleton(mirror);

        final Collection<Repository> selected = mirrorSelector.select(original, mirrors);
        assertFalse("Selection should remove original", selected.contains(originalRepo));
        assertTrue("Selection should contain mirror", selected.contains(mirroredRepo));
    }
}