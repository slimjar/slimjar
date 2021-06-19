package io.github.slimjar.relocation.manifest;

import io.github.slimjar.relocation.RelocationRule;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public interface ManifestFixer {
    void fix(final Path jarPath, final Collection<RelocationRule> rule) throws IOException, NoSuchAlgorithmException;
}
