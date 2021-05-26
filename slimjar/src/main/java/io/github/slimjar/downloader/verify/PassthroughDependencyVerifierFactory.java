package io.github.slimjar.downloader.verify;

import io.github.slimjar.resolver.DependencyResolver;

public final class PassthroughDependencyVerifierFactory implements DependencyVerifierFactory {
    @Override
    public DependencyVerifier create(DependencyResolver resolver) {
        return (file, dependency) -> file.exists();
    }
}
