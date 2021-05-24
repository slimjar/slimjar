package io.github.slimjar.relocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class PassthroughRelocator implements Relocator {
    @Override
    public void relocate(final File input, final File output) throws IOException {
        if (input.equals(output)) return;
        if (output.exists()) return;
        Files.copy(input.toPath(), output.toPath());
    }
}
