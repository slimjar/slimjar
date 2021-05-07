package io.github.vshnv.slimjar.downloader.output;

import io.github.vshnv.slimjar.downloader.strategy.FilePathStrategy;
import io.github.vshnv.slimjar.relocation.Relocator;
import io.github.vshnv.slimjar.resolver.data.Dependency;

import java.io.File;
import java.io.IOException;

public final class DependencyFileOutputWriterFactory implements OutputWriterFactory {
    private final FilePathStrategy outputFilePathStrategy;
    private final FilePathStrategy relocationFilePathStrategy;
    private final Relocator relocator;

    public DependencyFileOutputWriterFactory(final FilePathStrategy filePathStrategy, final FilePathStrategy relocationFilePathStrategy, final Relocator relocator) {
        this.relocationFilePathStrategy = relocationFilePathStrategy;
        this.outputFilePathStrategy = filePathStrategy;
        this.relocator = relocator;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public OutputWriter create(final Dependency dependency) {
        final File outputFile = outputFilePathStrategy.selectFileFor(dependency);
        outputFile.getParentFile().mkdirs();
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (System.getProperty("slimjarIgnoreRelocations").equals("1")) {
            return new SimpleFileOutputWriter(outputFile);
        } else {
            final File relocatedFile = relocationFilePathStrategy.selectFileFor(dependency);
            return new RelocatingFileOutputWriter(outputFile, relocatedFile, relocator);
        }
    }
}
