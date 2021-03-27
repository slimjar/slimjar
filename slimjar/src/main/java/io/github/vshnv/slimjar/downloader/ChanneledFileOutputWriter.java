package io.github.vshnv.slimjar.downloader;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ChanneledFileOutputWriter implements OutputWriter {
    private final File outputFile;
    public ChanneledFileOutputWriter(final File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void writeFrom(final InputStream inputStream, final long length) throws IOException {
        try (final ReadableByteChannel channel = Channels.newChannel(inputStream)) {
            try (final FileOutputStream output = new FileOutputStream(outputFile)) {
                output.getChannel().transferFrom(channel, 0, length);
            }
        }
    }
}
