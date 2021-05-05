package io.github.vshnv.slimjar.downloader.output;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface OutputWriter {
    URL writeFrom(final InputStream inputStream, final long length) throws IOException;
}
