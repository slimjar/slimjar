//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package io.github.slimjar.downloader.strategy;


import io.github.slimjar.downloader.output.DependencyOutputWriterFactory;
import io.github.slimjar.resolver.data.Dependency;

import java.io.File;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FolderedFilePathStrategy implements FilePathStrategy {
    private static final Logger LOGGER = Logger.getLogger(FolderedFilePathStrategy.class.getName());
    private static final String DEPENDENCY_FILE_FORMAT = "%s/%s/%s/%s/%3$s-%4$s.jar";
    private final File rootDirectory;


    private FolderedFilePathStrategy(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public File selectFileFor(Dependency dependency) {
        final String extendedVersion = Optional.ofNullable(dependency.getSnapshotId()).map(s -> "-" + s).orElse("");
        final String path = String.format(
                DEPENDENCY_FILE_FORMAT,
                rootDirectory.getPath(),
                dependency.getGroupId().replace('.','/'),
                dependency.getArtifactId(),
                dependency.getVersion() + extendedVersion
        );
        LOGGER.log(Level.FINEST, "Selected jar file for " + dependency.getArtifactId() + " at " + path);
        return new File(path);
    }

    public static FilePathStrategy createStrategy(final File rootDirectory) throws IllegalArgumentException {
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
