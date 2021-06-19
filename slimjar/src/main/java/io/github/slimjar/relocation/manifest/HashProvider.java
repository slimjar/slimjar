package io.github.slimjar.relocation.manifest;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public interface HashProvider {
    String hash(final InputStream inputStream, final String algorithm) throws NoSuchAlgorithmException, IOException;
}
