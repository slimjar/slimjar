package io.github.slimjar.resolver.enquirer;

import io.github.slimjar.resolver.data.Repository;

public interface RepositoryEnquirerFactory {
    RepositoryEnquirer create(final Repository repository);
}
