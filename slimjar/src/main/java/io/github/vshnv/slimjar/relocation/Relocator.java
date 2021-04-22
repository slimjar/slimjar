package io.github.vshnv.slimjar.relocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

public interface Relocator {
    void relocate(final File input, final File output, final Collection<RelocationRule> relocations) throws IOException;
}
