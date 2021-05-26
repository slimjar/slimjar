package io.github.slimjar.relocation.helper;


import io.github.slimjar.relocation.Relocator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

@FunctionalInterface
public interface RelocationHelperFactory {
    RelocationHelper create(final Relocator relocator) throws NoSuchAlgorithmException, IOException, URISyntaxException;
}
