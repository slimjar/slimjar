package io.github.slimjar.resolver;

import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirer;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;

public final class DummyRepositoryEnquirerFactory implements RepositoryEnquirerFactory {
    @Override
    public RepositoryEnquirer create(final Repository repository) {
        return new DummyRepositoryEnquirer();
    }
}
