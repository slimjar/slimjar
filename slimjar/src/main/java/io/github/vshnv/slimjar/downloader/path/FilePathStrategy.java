package io.github.vshnv.slimjar.downloader.path;

import io.github.vshnv.slimjar.resolver.data.Dependency;

import java.io.File;

public interface FilePathStrategy {
    File selectFileFor(final Dependency dependency);

    static FilePathStrategy createDefault(final File root) {
        return FolderedFilePathStrategy.createStrategy(root);
    }
}
