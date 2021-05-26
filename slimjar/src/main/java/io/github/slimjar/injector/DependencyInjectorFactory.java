package io.github.slimjar.injector;

import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.injector.helper.InjectionHelperFactory;
import io.github.slimjar.relocation.helper.RelocationHelper;

@FunctionalInterface
public interface DependencyInjectorFactory {
    DependencyInjector create(final InjectionHelperFactory injectionHelperFactory);
}
