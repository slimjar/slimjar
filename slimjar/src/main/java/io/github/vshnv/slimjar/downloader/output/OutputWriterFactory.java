package io.github.vshnv.slimjar.downloader.output;

public interface OutputWriterFactory {
    OutputWriter create(final String path);
}
