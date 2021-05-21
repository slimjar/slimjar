package io.github.slimjar.injector;

import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.relocation.RelocationHelper;

@FunctionalInterface
public interface DependencyInjectorFactory {
    DependencyInjector create(final DependencyDownloader downloader, final RelocationHelper relocationHelper);
}
