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
import io.github.slimjar.relocation.meta.MetaMediatorFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class VerifyingRelocationHelperFactory implements RelocationHelperFactory {
    private static final URL JAR_URL = VerifyingRelocationHelperFactory.class.getProtectionDomain().getCodeSource().getLocation();

    private final FilePathStrategy relocationFilePathStrategy;
    private final MetaMediatorFactory mediatorFactory;
    private final String selfHash;

    public VerifyingRelocationHelperFactory(final String selfHash, final FilePathStrategy relocationFilePathStrategy, final MetaMediatorFactory mediatorFactory) {
        this.relocationFilePathStrategy = relocationFilePathStrategy;
        this.mediatorFactory = mediatorFactory;
        this.selfHash = selfHash;
    }

    public VerifyingRelocationHelperFactory(final FileChecksumCalculator calculator, final FilePathStrategy relocationFilePathStrategy, final MetaMediatorFactory mediatorFactory) throws URISyntaxException, IOException {
        this(calculator.calculate(new File(JAR_URL.toURI())), relocationFilePathStrategy, mediatorFactory);
    }

    @Override
    public RelocationHelper create(final Relocator relocator) throws URISyntaxException, IOException, NoSuchAlgorithmException {
        return new VerifyingRelocationHelper(selfHash, relocationFilePathStrategy, relocator, mediatorFactory);
    }
}
