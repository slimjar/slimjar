package io.github.slimjar.relocation.meta;

import java.nio.file.Path;

public final class AttributeMetaMediatorFactory implements MetaMediatorFactory {
    @Override
    public MetaMediator create(final Path path) {
        return new AttributeMetaMediator(path);
    }
}
