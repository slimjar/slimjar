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

package io.github.slimjar.downloader.output;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.downloader.strategy.FilePathStrategy;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DependencyOutputWriterFactory implements OutputWriterFactory {
    private static final Logger LOGGER = Logger.getLogger(DependencyOutputWriterFactory.class.getName());
    private final FilePathStrategy outputFilePathStrategy;

    public DependencyOutputWriterFactory(final FilePathStrategy filePathStrategy) {
        this.outputFilePathStrategy = filePathStrategy;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public OutputWriter create(final Dependency dependency) {
        LOGGER.log(Level.FINEST, "Creating OutputWriter for {0}", dependency.getArtifactId());
        final File outputFile = outputFilePathStrategy.selectFileFor(dependency);
        outputFile.getParentFile().mkdirs();
        return new ChanneledFileOutputWriter(outputFile);
    }

    @Override
    public FilePathStrategy getStrategy() {
        return outputFilePathStrategy;
    }
}
