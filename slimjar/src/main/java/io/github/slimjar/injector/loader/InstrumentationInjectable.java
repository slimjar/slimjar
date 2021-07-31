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

package io.github.slimjar.injector.loader;

import io.github.slimjar.injector.agent.ByteBuddyInstrumentationFactory;
import io.github.slimjar.injector.agent.InstrumentationFactory;
import io.github.slimjar.logging.LogDispatcher;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.jar.JarFile;

public final class InstrumentationInjectable implements Injectable {



    private final Instrumentation instrumentation;

    public InstrumentationInjectable(final Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void inject(final URL url) throws IOException, URISyntaxException {
        instrumentation.appendToSystemClassLoaderSearch(new JarFile(new File(url.toURI())));
    }

    public static Injectable create(final Path downloadPath, final Collection<Repository> repositories) throws IOException, NoSuchAlgorithmException, ReflectiveOperationException, URISyntaxException {
        return create(new ByteBuddyInstrumentationFactory(ReflectiveJarRelocatorFacadeFactory.create(downloadPath, repositories)));
    }

    public static Injectable create(final InstrumentationFactory factory) throws IOException, NoSuchAlgorithmException, ReflectiveOperationException, URISyntaxException {
        return new InstrumentationInjectable(factory.create());
    }
}
