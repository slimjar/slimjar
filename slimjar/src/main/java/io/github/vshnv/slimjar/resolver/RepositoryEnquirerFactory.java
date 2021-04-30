package io.github.vshnv.slimjar.resolver;

import io.github.vshnv.slimjar.resolver.data.Repository;

public interface RepositoryEnquirerFactory {
    RepositoryEnquirer create(final Repository repository);
}
