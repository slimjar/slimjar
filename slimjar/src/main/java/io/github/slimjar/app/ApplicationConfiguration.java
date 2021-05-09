package io.github.slimjar.app;

import com.google.gson.Gson;
import io.github.slimjar.relocation.JarFileRelocator;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.resolver.CachingDependencyResolver;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.reader.DependencyDataProvider;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;
import io.github.slimjar.resolver.strategy.MavenPathResolutionStrategy;
import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.downloader.URLDependencyDownloader;
import io.github.slimjar.downloader.output.DependencyFileOutputWriterFactory;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.injector.DownloadingDependencyInjector;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import io.github.slimjar.resolver.enquirer.SimpleRepositoryEnquirerFactory;
import io.github.slimjar.resolver.mirrors.MirrorSelector;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.pinger.HttpURLPinger;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public final class ApplicationConfiguration {
    private static final Gson GSON = new Gson();
    private static final File DEFAULT_DOWNLOAD_DIRECTORY;

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

    public static ApplicationConfiguration createDefault(final String applicationName) throws IOException {
        final URL depFileURL = ApplicationConfiguration.class.getClassLoader().getResource("slimjar.json");
        if (depFileURL == null) throw new IllegalStateException("Could not find generated slimjar.json! Did you use the slimjar plugin to build?");
        return createDefault(depFileURL, DEFAULT_DOWNLOAD_DIRECTORY, applicationName);
    }

    public static ApplicationConfiguration createDefault(final URL depFileURL, final File downloadDirectory, final String applicationName) throws IOException {
        final DependencyDataProviderFactory dependencyDataProviderFactory = new DependencyDataProviderFactory(GSON);
        final DependencyDataProvider dependencyDataProvider = dependencyDataProviderFactory.create(depFileURL);
        final DependencyData data = dependencyDataProvider.get();

        final MirrorSelector mirrorSelector = new SimpleMirrorSelector();
        final Collection<Repository> repositories = mirrorSelector.select(data.getRepositories(), data.getMirrors());

        final Relocator relocator = new JarFileRelocator(data.getRelocations());
        final FilePathStrategy filePathStrategy = FilePathStrategy.createDefault(downloadDirectory);
        final FilePathStrategy relocationFilePathStrategy = FilePathStrategy.createRelocationStrategy(downloadDirectory, applicationName);
        final OutputWriterFactory outputWriterFactory = new DependencyFileOutputWriterFactory(filePathStrategy, relocationFilePathStrategy, relocator);
        final PathResolutionStrategy pathResolutionStrategy = new MavenPathResolutionStrategy();
        final URLPinger urlPinger = new HttpURLPinger();
        final RepositoryEnquirerFactory repositoryEnquirerFactory = new SimpleRepositoryEnquirerFactory(pathResolutionStrategy, urlPinger);
        final DependencyResolver dependencyResolver = new CachingDependencyResolver(repositories, repositoryEnquirerFactory);
        final DependencyDownloader dependencyDownloader = new URLDependencyDownloader(outputWriterFactory, dependencyResolver);
        final DependencyInjector dependencyInjector = new DownloadingDependencyInjector(dependencyDownloader);
        return new ApplicationConfiguration(dependencyInjector, data);
    }
}
