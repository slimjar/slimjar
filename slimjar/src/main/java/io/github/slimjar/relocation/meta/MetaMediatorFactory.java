package io.github.slimjar.relocation.meta;

import java.nio.file.Path;

@FunctionalInterface
public interface MetaMediatorFactory {
    MetaMediator create(final Path path);
}
