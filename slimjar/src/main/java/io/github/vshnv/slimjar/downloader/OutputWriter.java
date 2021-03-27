package io.github.vshnv.slimjar.downloader;

import java.io.IOException;
import java.io.InputStream;

public interface OutputWriter {
    void writeFrom(final InputStream inputStream, final long length) throws IOException;
}
