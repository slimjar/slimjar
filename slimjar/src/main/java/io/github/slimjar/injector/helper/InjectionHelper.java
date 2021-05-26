package io.github.slimjar.injector.helper;


import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.relocation.helper.RelocationHelper;
import io.github.slimjar.resolver.data.Dependency;

import java.io.File;
import java.io.IOException;

public final class InjectionHelper {
    private final DependencyDownloader dependencyDownloader;
    private final RelocationHelper relocationHelper;

    public InjectionHelper(final DependencyDownloader dependencyDownloader, final RelocationHelper relocationHelper) {
        this.dependencyDownloader = dependencyDownloader;
        this.relocationHelper = relocationHelper;
    }

    public File fetch(final Dependency dependency) throws IOException, ReflectiveOperationException {
        final File downloaded = dependencyDownloader.download(dependency);
        return relocationHelper.relocate(dependency, downloaded);
    }
}
