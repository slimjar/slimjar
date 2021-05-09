package io.github.slimjar.downloader.strategy;

import io.github.slimjar.resolver.data.Dependency;

import java.io.File;

public interface FilePathStrategy {
    File selectFileFor(final Dependency dependency);

    static FilePathStrategy createDefault(final File root) {
        return FolderedFilePathStrategy.createStrategy(root);
    }
    static FilePathStrategy createRelocationStrategy(final File root, final String applicationName) {
        return RelocationFilePathStrategy.createStrategy(root, applicationName);
    }
}
