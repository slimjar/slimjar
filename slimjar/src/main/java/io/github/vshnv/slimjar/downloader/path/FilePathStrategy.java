package io.github.vshnv.slimjar.downloader.path;

import io.github.vshnv.slimjar.data.Dependency;

import java.io.File;

public interface FilePathStrategy {
    File selectFileFor(final Dependency dependency);
}
