package io.github.vshnv.slimjar.downloader.output;

import io.github.vshnv.slimjar.downloader.path.FilePathStrategy;
import io.github.vshnv.slimjar.resolver.data.Dependency;

import java.io.File;
import java.io.IOException;

public final class DependencyFileOutputWriterFactory implements OutputWriterFactory {
    private final FilePathStrategy filePathStrategy;

    public DependencyFileOutputWriterFactory(FilePathStrategy filePathStrategy) {
        this.filePathStrategy = filePathStrategy;
    }

    @Override
    public OutputWriter create(final Dependency dependency) {
        final File rootDirectory = filePathStrategy.selectFileFor(dependency);
        rootDirectory.getParentFile().mkdirs();
        try {
            rootDirectory.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ChanneledFileOutputWriter(rootDirectory);
    }
}
