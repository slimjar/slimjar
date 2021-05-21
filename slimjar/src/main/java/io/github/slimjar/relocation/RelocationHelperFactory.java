package io.github.slimjar.relocation;


import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

@FunctionalInterface
public interface RelocationHelperFactory {
    RelocationHelper create(final Relocator relocator) throws NoSuchAlgorithmException, IOException, URISyntaxException;
}
