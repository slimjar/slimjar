package io.github.slimjar.downloader;

import io.github.slimjar.resolver.data.Dependency;

import java.io.IOException;
import java.net.URL;

public interface DependencyDownloader {
    URL download(final Dependency dependency) throws IOException;
}
