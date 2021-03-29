package io.github.vshnv.slimjar.downloader;

import java.io.File;

public final class FileOutputWriterFactory implements OutputWriterFactory {
    private final File outputDirectory;

    public FileOutputWriterFactory(final File outputDirectory) {
        this.outputDirectory = outputDirectory;
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
    }

    @Override
    public OutputWriter create(final String path) {
        final File downloadLocation = new File(outputDirectory, String.format("%s.jar", path));
        return new ChanneledFileOutputWriter(downloadLocation);
    }
}
