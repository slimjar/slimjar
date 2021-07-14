package io.github.slimjar.relocation.meta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FlatFileMetaMediator implements MetaMediator {
    private final Path metaFolderPath;

    public FlatFileMetaMediator(final Path metaFolderPath) {
        this.metaFolderPath = metaFolderPath;
    }

    @Override
    public String readAttribute(String name) throws IOException {
        final Path attributeFile = metaFolderPath.resolve(name);
        if (!Files.exists(attributeFile) || Files.isDirectory(attributeFile)) {
            return null;
        }
        return new String(Files.readAllBytes(attributeFile));
    }

    @Override
    public void writeAttribute(String name, String value) throws IOException {
        final Path attributeFile = metaFolderPath.resolve(name);
        Files.deleteIfExists(attributeFile);
        Files.createFile(attributeFile);
        Files.write(attributeFile, value.getBytes());
    }
}
