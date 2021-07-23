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

import io.github.slimjar.app.Application;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.injector.loader.InjectableClassLoader;
import io.github.slimjar.injector.loader.IsolatedInjectableClassLoader;
import io.github.slimjar.resolver.ResolutionResult;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.dependency.DependencyDataProvider;
import io.github.slimjar.resolver.reader.resolution.PreResolutionDataProvider;
import io.github.slimjar.util.Modules;
import io.github.slimjar.util.Parameters;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;

public final class IsolatedApplicationBuilder extends ApplicationBuilder {
    private final IsolationConfiguration isolationConfiguration;
    private final Object[] arguments;

    public IsolatedApplicationBuilder(final String applicationName, final IsolationConfiguration isolationConfiguration, final Object[] arguments) {
        super(applicationName);
        this.isolationConfiguration = isolationConfiguration;
        this.arguments = arguments.clone();
    }

    @Override
    public Application buildApplication() throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException {
        final DependencyInjector injector = createInjector();
        final URL[] moduleUrls = Modules.extract(isolationConfiguration.getModuleExtractor(), isolationConfiguration.getModules());

        final InjectableClassLoader classLoader = new IsolatedInjectableClassLoader(moduleUrls, isolationConfiguration.getParentClassloader(), Collections.singleton(Application.class));

        final DependencyDataProvider dataProvider = getDataProviderFactory().create(getDependencyFileUrl());
        final DependencyData selfDependencyData = dataProvider.get();

        final PreResolutionDataProvider preResolutionDataProvider = getPreResolutionDataProviderFactory().create(getPreResolutionFileUrl());
        final Map<String, ResolutionResult> preResolutionResultMap = preResolutionDataProvider.get();

        injector.inject(classLoader, selfDependencyData, preResolutionResultMap);

        for (final URL module : moduleUrls) {
            final DependencyDataProvider moduleDataProvider = getModuleDataProviderFactory().create(module);
            final DependencyData dependencyData = moduleDataProvider.get();
            // TODO:: fetch isolated pre-resolutions
            injector.inject(classLoader, dependencyData, preResolutionResultMap);
        }

        final Class<Application> applicationClass = (Class<Application>) Class.forName(isolationConfiguration.getApplicationClass(), true, classLoader);

        // TODO:: Fix constructor resolution
        return applicationClass.getConstructor(Parameters.typesFrom(arguments)).newInstance(arguments);
    }

}
