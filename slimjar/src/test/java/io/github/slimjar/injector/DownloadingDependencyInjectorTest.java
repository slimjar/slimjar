package io.github.slimjar.injector;


import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.injector.loader.Injectable;
import junit.framework.TestCase;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

public class DownloadingDependencyInjectorTest extends TestCase {
    public void testDownloadingDependencyInjector() throws IOException {
        final URL testURL = this.getClass().getResource("sample.json");
        final URL[] result = {null};
        Injectable injectable = url -> result[0] = url;
        DependencyDownloader dependencyDownloader = dependency -> testURL;
        DependencyInjector dependencyInjector = new DownloadingDependencyInjector(dependencyDownloader);
        dependencyInjector.inject(injectable, Collections.emptySet());
        assertEquals(result[0], testURL);
    }
}