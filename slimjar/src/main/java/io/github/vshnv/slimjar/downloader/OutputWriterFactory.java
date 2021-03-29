package io.github.vshnv.slimjar.downloader;

public interface OutputWriterFactory {
    OutputWriter create(final String path);
}
