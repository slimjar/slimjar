package io.github.slimjar.injector;

import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.injector.helper.InjectionHelperFactory;
import io.github.slimjar.relocation.helper.RelocationHelper;

public final class SimpleDependencyInjectorFactory implements DependencyInjectorFactory {
    @Override
    public DependencyInjector create(final InjectionHelperFactory injectionHelperFactory) {
        return new SimpleDependencyInjector(injectionHelperFactory);
    }
}
