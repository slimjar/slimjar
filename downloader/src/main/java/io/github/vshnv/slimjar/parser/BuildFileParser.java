package io.github.vshnv.slimjar.parser;

import java.io.InputStream;

public interface BuildFileParser {
    BuildData parse(InputStream inputStream) throws BuildFileParseException;
}
