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

package io.github.slimjar.app;

import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.app.builder.IsolationConfiguration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

@Deprecated
public final class ApplicationFactory {
    public Application createIsolatedApplication(final Collection<String> moduleNames, final String fqClassName, final Object... args) throws ClassCastException {
        return createIsolatedApplication(moduleNames, fqClassName, ClassLoader.getSystemClassLoader().getParent(), args);
    }

    public Application createIsolatedApplication(final String applicationName, final Collection<String> moduleNames, final String fqClassName, final ClassLoader parent, final Object... args) throws ReflectiveOperationException, ClassCastException, IOException, URISyntaxException, NoSuchAlgorithmException {
        final IsolationConfiguration config = new IsolationConfiguration.Builder()
                .modules(moduleNames).applicationClass(fqClassName).parentClassLoader(parent).build();
        return ApplicationBuilder.isolated(applicationName, config, args).build();
    }

    public Application createAppendingApplication(final String applicationName) throws URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException, IOException {
        return ApplicationBuilder.appending(applicationName).build();
    }
}
