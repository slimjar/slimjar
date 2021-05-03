package io.github.vshnv.slimjar.downloader.path;


import io.github.vshnv.slimjar.resolver.data.Dependency;

import java.io.File;

public final class FolderedFilePathStrategy implements FilePathStrategy {
    private static final String DEPENDENCY_FILE_FORMAT = "%s/%s/%s/%s/%3$s-%4$s.jar";
    private final File rootDirectory;


    private FolderedFilePathStrategy(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public File selectFileFor(Dependency dependency) {
        final String path = String.format(
                DEPENDENCY_FILE_FORMAT,
                rootDirectory.getPath(),
                dependency.getGroupId().replace('.','/'),
                dependency.getArtifactId(),
                dependency.getVersion()
        );
        return new File(path);
    }

    static FilePathStrategy createStrategy(File rootDirectory) throws IllegalArgumentException {
        if (!rootDirectory.exists()) {
            boolean created = rootDirectory.mkdirs();
            if (!created) {
                throw new IllegalArgumentException("Could not create specified directory: " + rootDirectory);
            }
        }
        if (!rootDirectory.isDirectory()) {
            throw new IllegalArgumentException("Expecting a directory for download root! " + rootDirectory);
        }
        return new FolderedFilePathStrategy(rootDirectory);
    }
}
