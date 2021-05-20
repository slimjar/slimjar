package io.github.slimjar.relocation.meta;

import java.io.IOException;

public interface MetaMediator {
    String readAttribute(final String name) throws IOException;
    void writeAttribute(final String name, final String value) throws IOException;
}
