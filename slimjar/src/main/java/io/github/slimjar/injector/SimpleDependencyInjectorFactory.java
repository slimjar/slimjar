package io.github.slimjar.injector;

import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.relocation.RelocationHelper;

public final class SimpleDependencyInjectorFactory implements DependencyInjectorFactory {
    @Override
    public DependencyInjector create(final DependencyDownloader downloader, final RelocationHelper relocationHelper) {
        return new SimpleDependencyInjector(downloader, relocationHelper);
    }
}
