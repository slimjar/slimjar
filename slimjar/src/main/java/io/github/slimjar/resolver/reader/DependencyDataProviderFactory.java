package io.github.slimjar.resolver.reader;

import java.net.URL;

@FunctionalInterface
public interface DependencyDataProviderFactory {
    DependencyDataProvider create(final URL dependencyFileURL);
}
