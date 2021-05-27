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

package io.github.slimjar.relocation;

import io.github.slimjar.relocation.facade.JarRelocatorFacade;
import io.github.slimjar.relocation.facade.JarRelocatorFacadeFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public final class JarFileRelocator implements Relocator {
    private final Collection<RelocationRule> relocations;
    private final JarRelocatorFacadeFactory relocatorFacadeFactory;

    public JarFileRelocator(final Collection<RelocationRule> relocations, final JarRelocatorFacadeFactory relocatorFacadeFactory) {
        this.relocations = relocations;
        this.relocatorFacadeFactory = relocatorFacadeFactory;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void relocate(File input, File output) throws IOException, ReflectiveOperationException {
        output.getParentFile().mkdirs();
        output.createNewFile();
        final JarRelocatorFacade jarRelocator = relocatorFacadeFactory.createFacade(input,output, relocations);
        jarRelocator.run();
    }
}
