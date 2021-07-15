package io.github.slimjar.relocation.meta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FlatFileMetaMediatorFactory implements MetaMediatorFactory {
    @Override
    public MetaMediator create(final Path path) {
        final Path metaPath = path.getParent().resolve(path.getFileName().toString() + ".slimjar_meta");
        if (!Files.exists(metaPath)) {
            try {
                Files.createDirectories(metaPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
        return new FlatFileMetaMediator(metaPath);
    }
}
