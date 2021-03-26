package io.github.vshnv.slimjar.parser;

public final class BuildFileParseException extends Exception {
    public BuildFileParseException(Exception exception) {
        super("Failed to parse build file!", exception);
    }
}
