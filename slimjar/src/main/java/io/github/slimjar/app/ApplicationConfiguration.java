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

import io.github.slimjar.app.external.DependencyProvider;
import io.github.slimjar.app.external.DependencyProviderFactory;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.relocation.VerifyingRelocationHelper;
import io.github.slimjar.relocation.meta.AttributeMetaMediatorFactory;
import io.github.slimjar.resolver.CachingDependencyResolver;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.reader.DependencyDataProvider;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;
import io.github.slimjar.resolver.strategy.MavenPathResolutionStrategy;
import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.downloader.URLDependencyDownloader;
import io.github.slimjar.downloader.output.DependencyOutputWriterFactory;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.injector.SimpleDependencyInjector;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import io.github.slimjar.resolver.enquirer.PingingRepositoryEnquirerFactory;
import io.github.slimjar.resolver.mirrors.MirrorSelector;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.pinger.HttpURLPinger;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

@Deprecated
public final class ApplicationConfiguration {
    public static final File DEFAULT_DOWNLOAD_DIRECTORY;

    static {
        final String userHome = System.getProperty("user.home");
        final String defaultPath = String.format("%s/.slimjar", userHome);
        DEFAULT_DOWNLOAD_DIRECTORY = new File(defaultPath);
    }

    private final DependencyInjector dependencyInjector;
    private final DependencyData dependencyData;

    public ApplicationConfiguration(final DependencyInjector dependencyInjector, final DependencyData dependencyData) {
        this.dependencyInjector = dependencyInjector;
        this.dependencyData = dependencyData;
    }

    public DependencyInjector getDependencyInjector() {
        return dependencyInjector;
    }

    public DependencyData getDependencyData() {
        return dependencyData;
    }

    public static ApplicationConfiguration createDefault(final String applicationName) throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException {
        final URL depFileURL = ApplicationConfiguration.class.getClassLoader().getResource("slimjar.json");
        if (depFileURL == null) throw new IllegalStateException("Could not find generated slimjar.json! Did you use the slimjar plugin to build?");
        return createDefault(depFileURL, DEFAULT_DOWNLOAD_DIRECTORY, applicationName);
    }

    public static ApplicationConfiguration createDefault(final URL depFileURL, final File downloadDirectory, final String applicationName) throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException {
        final DependencyProvider dependencyProvider = DependencyProviderFactory.createExternalDependencyProvider();
        final DependencyDataProviderFactory dependencyDataProviderFactory = dependencyProvider.createDependencyDataProviderFactory();
        final DependencyDataProvider dependencyDataProvider = dependencyDataProviderFactory.create(depFileURL);
        final DependencyData data = dependencyDataProvider.get();
        return createFrom(data, downloadDirectory, applicationName);
    }

    public static ApplicationConfiguration createFrom(final DependencyData data, final File downloadDirectory, final String applicationName) throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException {
        final DependencyProvider dependencyProvider = DependencyProviderFactory.createExternalDependencyProvider();
        final MirrorSelector mirrorSelector = new SimpleMirrorSelector();
        final Collection<Repository> repositories = mirrorSelector.select(data.getRepositories(), data.getMirrors());

        final Relocator relocator = dependencyProvider.createRelocator(data.getRelocations());
        final FilePathStrategy filePathStrategy = FilePathStrategy.createDefault(downloadDirectory);
        final FilePathStrategy relocationFilePathStrategy = FilePathStrategy.createRelocationStrategy(downloadDirectory, applicationName);
        final OutputWriterFactory outputWriterFactory = new DependencyOutputWriterFactory(filePathStrategy);
        final PathResolutionStrategy pathResolutionStrategy = new MavenPathResolutionStrategy();
        final URLPinger urlPinger = new HttpURLPinger();
        final RepositoryEnquirerFactory repositoryEnquirerFactory = new PingingRepositoryEnquirerFactory(pathResolutionStrategy, urlPinger);
        final DependencyResolver dependencyResolver = new CachingDependencyResolver(repositories, repositoryEnquirerFactory);
        final DependencyDownloader dependencyDownloader = new URLDependencyDownloader(outputWriterFactory, dependencyResolver);
        final DependencyInjector dependencyInjector = new SimpleDependencyInjector(dependencyDownloader, new VerifyingRelocationHelper(relocationFilePathStrategy, relocator, new AttributeMetaMediatorFactory()));
        return new ApplicationConfiguration(dependencyInjector, data);
    }
}
