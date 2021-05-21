package io.github.slimjar.resolver.reader;

import java.net.URL;

public interface DependencyDataProviderFactory {
    @Deprecated
    DependencyDataProvider create(final URL dependencyFileURL);
    DependencyDataProvider forFile(final URL dependencyFileURL);
    DependencyDataProvider forModule(final URL dependencyFileURL);
}
