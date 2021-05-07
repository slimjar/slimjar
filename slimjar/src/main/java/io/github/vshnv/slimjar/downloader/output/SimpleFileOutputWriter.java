package io.github.vshnv.slimjar.downloader.output;

import io.github.vshnv.slimjar.relocation.Relocator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public final class SimpleFileOutputWriter implements OutputWriter {

    private final File outputFile;

    public SimpleFileOutputWriter(final File outputFile) {
        this.outputFile = outputFile;
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
        return outputFile.toURI().toURL();
    }
}