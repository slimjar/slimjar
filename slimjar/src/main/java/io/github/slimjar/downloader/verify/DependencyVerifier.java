package io.github.slimjar.downloader.verify;

import io.github.slimjar.resolver.data.Dependency;

import java.io.File;
import java.io.IOException;

public interface DependencyVerifier {
    boolean verify(final File file, final Dependency dependency) throws IOException;
}
