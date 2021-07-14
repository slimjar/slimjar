package io.github.slimjar.relocation.meta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FlatFileMetaMediatorFactory implements MetaMediatorFactory {
    @Override
    public MetaMediator create(final Path path) throws IOException {
        final Path metaPath = path.getParent().resolve(path.getFileName().toString() + ".slimjar_meta");
        if (!Files.exists(metaPath)) {
            Files.createDirectories(metaPath);
        }
        return new FlatFileMetaMediator(metaPath);
    }
}
