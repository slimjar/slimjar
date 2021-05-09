package io.github.slimjar.downloader.output;


import io.github.slimjar.resolver.data.Dependency;

public interface OutputWriterFactory {
    OutputWriter create(final Dependency param);
}
