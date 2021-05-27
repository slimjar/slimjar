package io.github.slimjar.downloader.strategy;

import io.github.slimjar.resolver.data.Dependency;

import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ChecksumFilePathStrategy implements FilePathStrategy {
    private static final Logger LOGGER = Logger.getLogger(FolderedFilePathStrategy.class.getName());
    private static final String DEPENDENCY_FILE_FORMAT = "%s/%s/%s/%s/%3$s-%4$s.jar.%5$s";
    private final File rootDirectory;
    private final String algorithm;


    private ChecksumFilePathStrategy(final File rootDirectory, final String algorithm) {
        this.rootDirectory = rootDirectory;
        this.algorithm = algorithm.replaceAll("[ -]", "").toLowerCase(Locale.ENGLISH);
    }

    @Override
    public File selectFileFor(Dependency dependency) {
        final String path = String.format(
                DEPENDENCY_FILE_FORMAT,
                rootDirectory.getPath(),
                dependency.getGroupId().replace('.','/'),
                dependency.getArtifactId(),
                dependency.getVersion(),
                algorithm
        );
        LOGGER.log(Level.FINEST, "Selected checksum file for " + dependency.getArtifactId() + " at " + path);
        return new File(path);
    }

    public static FilePathStrategy createStrategy(final File rootDirectory, final String algorithm) throws IllegalArgumentException {
        if (!rootDirectory.exists()) {
            boolean created = rootDirectory.mkdirs();
            if (!created) {
                throw new IllegalArgumentException("Could not create specified directory: " + rootDirectory);
            }
        }
        if (!rootDirectory.isDirectory()) {
            throw new IllegalArgumentException("Expecting a directory for download root! " + rootDirectory);
        }
        return new ChecksumFilePathStrategy(rootDirectory, algorithm);
    }
}
