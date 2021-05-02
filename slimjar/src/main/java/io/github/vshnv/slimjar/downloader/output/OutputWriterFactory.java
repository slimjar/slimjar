package io.github.vshnv.slimjar.downloader.output;

import io.github.vshnv.slimjar.data.Dependency;

public interface OutputWriterFactory {
    OutputWriter create(final Dependency param);
}
