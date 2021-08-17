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

package io.github.slimjar.relocation.helper;

import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.downloader.verify.FileChecksumCalculator;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.relocation.meta.MetaMediator;
import io.github.slimjar.relocation.meta.MetaMediatorFactory;
import io.github.slimjar.resolver.data.Dependency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.security.NoSuchAlgorithmException;

public final class VerifyingRelocationHelper implements RelocationHelper {
    private final FilePathStrategy outputFilePathStrategy;
    private final Relocator relocator;
    private final String selfHash;
    private final MetaMediatorFactory mediatorFactory;

    public VerifyingRelocationHelper(final String selfHash, final FilePathStrategy outputFilePathStrategy, final Relocator relocator, final MetaMediatorFactory mediatorFactory) throws URISyntaxException, NoSuchAlgorithmException, IOException {
        this.mediatorFactory = mediatorFactory;
        this.outputFilePathStrategy = outputFilePathStrategy;
        this.relocator = relocator;
        this.selfHash = selfHash;
    }

    @Override
    public File relocate(final Dependency dependency, final File file) throws IOException, ReflectiveOperationException {
        final File relocatedFile = outputFilePathStrategy.selectFileFor(dependency);
        final MetaMediator metaMediator = mediatorFactory.create(relocatedFile.toPath());
        if (relocatedFile.exists()) {
            try {
                final String ownerHash = metaMediator.readAttribute("slimjar.owner");
                if (selfHash != null && ownerHash != null && selfHash.trim().equals(ownerHash.trim())) {
                    return relocatedFile;
                }
            } catch (final Exception exception) {
                // Possible incomplete relocation present.
                // todo: Log incident
                //noinspection ResultOfMethodCallIgnored
                relocatedFile.delete();
            }
        }
        relocator.relocate(file, relocatedFile);
        metaMediator.writeAttribute("slimjar.owner", selfHash);
        return relocatedFile;
    }
}
