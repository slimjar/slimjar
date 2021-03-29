package io.github.vshnv.slimjar.downloader;

import java.io.File;
import java.util.function.Function;

public final class FileOutputWriterProducer implements OutputWriterFactory {
    private final File outputDirectory;

    public FileOutputWriterProducer(final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public OutputWriter create(final String path) {
        final File downloadLocation = new File(outputDirectory, String.format("%s.jar", path));
        return new ChanneledFileOutputWriter(downloadLocation);
    }
}
