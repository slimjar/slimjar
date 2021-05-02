package io.github.vshnv.slimjar.downloader.output;

import io.github.vshnv.slimjar.data.Dependency;
import io.github.vshnv.slimjar.downloader.path.FilePathStrategy;

import java.io.File;

public final class DependencyFileOutputWriterFactory implements OutputWriterFactory {
    private final FilePathStrategy filePathStrategy;

    public DependencyFileOutputWriterFactory(FilePathStrategy filePathStrategy) {
        this.filePathStrategy = filePathStrategy;
    }

    @Override
    public OutputWriter create(final Dependency dependency) {
        final File rootDirectory = filePathStrategy.selectFileFor(dependency);
        return new ChanneledFileOutputWriter(rootDirectory);
    }
}
