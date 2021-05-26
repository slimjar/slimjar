//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package io.github.slimjar.downloader.output;


import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ChanneledFileOutputWriter implements OutputWriter {
    private static final Logger LOGGER = Logger.getLogger(ChanneledFileOutputWriter.class.getName());
    private final File outputFile;

    public ChanneledFileOutputWriter(final File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public File writeFrom(final InputStream inputStream, final long length) throws IOException {
        LOGGER.log(Level.FINE, "Attempting to write from inputStream...");
        if (!outputFile.exists()) {
            LOGGER.log(Level.FINE, "Writing {0} bytes...", length);
            try (final ReadableByteChannel channel = Channels.newChannel(inputStream)) {
                try (final FileOutputStream output = new FileOutputStream(outputFile)) {
                    output.getChannel().transferFrom(channel, 0, length);
                }
            }
        }
        inputStream.close();
        return outputFile;
    }
}
