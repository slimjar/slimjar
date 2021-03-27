package io.github.vshnv.slimjar.downloader;

import io.github.vshnv.slimjar.data.Dependency;

import java.io.IOException;
import java.net.URL;

public interface DependencyDownloader {
    URL download(Dependency dependency) throws IOException;
}
