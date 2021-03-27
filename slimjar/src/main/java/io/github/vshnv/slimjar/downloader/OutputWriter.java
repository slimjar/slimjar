package io.github.vshnv.slimjar.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface OutputWriter {
    void writeFrom(InputStream inputStream, long length) throws IOException;
}
