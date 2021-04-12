package io.github.vshnv.slimjar.downloader.output;

import java.io.File;

public final class FileOutputWriterFactory implements OutputWriterFactory {
    private final File outputDirectory;

    public FileOutputWriterFactory(final File outputDirectory) {
        this.outputDirectory = outputDirectory;
        if (!outputDirectory.exists()) {
            boolean success = outputDirectory.mkdirs();
            if (!success) {
                throw new IllegalStateException(
                        "Could not create output directory at " +
                                outputDirectory.getAbsolutePath()
                );
            }
        }
    }

    @Override
    public OutputWriter create(final String path) {
        final File downloadLocation = new File(outputDirectory, String.format("%s.jar", path));
        return new ChanneledFileOutputWriter(downloadLocation);
    }
}
