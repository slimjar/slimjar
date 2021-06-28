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

package io.github.slimjar.app.builder;

import io.github.slimjar.app.AppendingApplication;
import io.github.slimjar.app.Application;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.injector.loader.*;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.DependencyDataProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.security.NoSuchAlgorithmException;

public final class InjectingApplicationBuilder extends ApplicationBuilder {
    private final Injectable classLoader;

    public InjectingApplicationBuilder(final String applicationName, final Injectable classLoader) {
        super(applicationName);
        this.classLoader = classLoader;
    }

    @Override
    public Application build() throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException {
        final DependencyDataProvider dataProvider = getDataProviderFactory().create(getDependencyFileUrl());
        final DependencyData dependencyData = dataProvider.get();
        final DependencyInjector dependencyInjector = createInjector();
        dependencyInjector.inject(classLoader, dependencyData);
        return new AppendingApplication();
    }

    public static ApplicationBuilder createAppending(final String applicationName) throws ReflectiveOperationException, NoSuchAlgorithmException, IOException, URISyntaxException {
        final ClassLoader classLoader = ApplicationBuilder.class.getClassLoader();
        final Injectable injectable = InjectableFactory.create(classLoader);
        return new InjectingApplicationBuilder(applicationName, injectable);
    }
}

