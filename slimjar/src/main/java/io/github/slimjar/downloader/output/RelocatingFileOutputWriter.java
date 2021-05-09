package io.github.slimjar.downloader.output;

import io.github.slimjar.relocation.Relocator;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public final class RelocatingFileOutputWriter implements OutputWriter {

    private final File outputFile;
    private final File relocatedFile;
    private final Relocator relocator;

    public RelocatingFileOutputWriter(final File outputFile, final File relocatedFile, final Relocator relocator) {
        this.outputFile = outputFile;
        this.relocatedFile = relocatedFile;
        this.relocator = relocator;
    }

    @Override
    public URL writeFrom(final InputStream inputStream, final long length) throws IOException {
        if (!outputFile.exists()) {
            try (final ReadableByteChannel channel = Channels.newChannel(inputStream)) {
                try (final FileOutputStream output = new FileOutputStream(outputFile)) {
                    output.getChannel().transferFrom(channel, 0, length);
                }
            }
        }
        inputStream.close();
        relocator.relocate(outputFile, relocatedFile);
        return relocatedFile.toURI().toURL();
    }
}
