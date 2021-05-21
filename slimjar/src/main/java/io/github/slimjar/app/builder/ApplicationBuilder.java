package io.github.slimjar.app.builder;

import io.github.slimjar.app.Application;
import io.github.slimjar.app.external.DependencyProvider;
import io.github.slimjar.app.external.DependencyProviderFactory;
import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.downloader.DependencyDownloaderFactory;
import io.github.slimjar.downloader.URLDependencyDownloaderFactory;
import io.github.slimjar.downloader.output.DependencyOutputWriterFactory;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.injector.DependencyInjectorFactory;
import io.github.slimjar.injector.SimpleDependencyInjectorFactory;
import io.github.slimjar.relocation.RelocationHelper;
import io.github.slimjar.relocation.RelocationHelperFactory;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.relocation.VerifyingRelocationHelperFactory;
import io.github.slimjar.relocation.meta.AttributeMetaMediatorFactory;
import io.github.slimjar.relocation.meta.MetaMediatorFactory;
import io.github.slimjar.resolver.CachingDependencyResolverFactory;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.DependencyResolverFactory;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.enquirer.PingingRepositoryEnquirerFactory;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import io.github.slimjar.resolver.mirrors.MirrorSelector;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.pinger.HttpURLPinger;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.resolver.reader.DependencyDataProvider;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;
import io.github.slimjar.resolver.strategy.MavenPathResolutionStrategy;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Objects;

public abstract class ApplicationBuilder {
    private static final Path DEFAULT_DOWNLOAD_DIRECTORY;

    static {
        final String userHome = System.getProperty("user.home");
        final String defaultPath = String.format("%s/.slimjar", userHome);
        DEFAULT_DOWNLOAD_DIRECTORY = new File(defaultPath).toPath();
    }

    private final String applicationName;
    private URL dependencyFileUrl;
    private Path downloadDirectoryPath;
    private DependencyProvider dependencyProvider;
    private RelocationHelperFactory relocationHelperFactory;
    private DependencyInjectorFactory injectorFactory;
    private DependencyResolverFactory resolverFactory;
    private RepositoryEnquirerFactory enquirerFactory;
    private DependencyDownloaderFactory downloaderFactory;
    private MirrorSelector mirrorSelector;

    protected ApplicationBuilder(final String applicationName) {
        this.applicationName = Objects.requireNonNull(applicationName, "Requires non-null application name!");
    }

    public static ApplicationBuilder isolated(final String name, final IsolationConfiguration config, Object[] args) {
        return new IsolatedApplicationBuilder(name, config, args);
    }

    public static ApplicationBuilder appending(final String name, final URLClassLoader classLoader) {
        return new AppendingApplicationBuilder(name, classLoader);
    }

    public final ApplicationBuilder dependencyFileUrl(final URL dependencyFileUrl) {
        this.dependencyFileUrl = dependencyFileUrl;
        return this;
    }

    public final ApplicationBuilder downloadDirectoryPath(final Path downloadDirectoryPath) {
        this.downloadDirectoryPath = downloadDirectoryPath;
        return this;
    }

    public final ApplicationBuilder relocationHelperFactory(final RelocationHelperFactory relocationHelperFactory) {
        this.relocationHelperFactory = relocationHelperFactory;
        return this;
    }

    public final ApplicationBuilder injectorFactory(final DependencyInjectorFactory injectorFactory) {
        this.injectorFactory = injectorFactory;
        return this;
    }

    public final ApplicationBuilder resolverFactory(final DependencyResolverFactory resolverFactory) {
        this.resolverFactory = resolverFactory;
        return this;
    }

    public final ApplicationBuilder enquirerFactory(final RepositoryEnquirerFactory enquirerFactory) {
        this.enquirerFactory = enquirerFactory;
        return this;
    }

    public final ApplicationBuilder downloaderFactory(final DependencyDownloaderFactory downloaderFactory) {
        this.downloaderFactory = downloaderFactory;
        return this;
    }

    public final ApplicationBuilder mirrorSelector(final MirrorSelector mirrorSelector) {
        this.mirrorSelector = mirrorSelector;
        return this;
    }

    protected final String getApplicationName() {
        return applicationName;
    }

    protected final URL getDependencyFileUrl() {
        if (dependencyFileUrl == null) {
            this.dependencyFileUrl = getClass().getResource("slimjar.json");
        }
        return dependencyFileUrl;
    }

    protected final Path getDownloadDirectoryPath() {
        if (downloadDirectoryPath == null) {
            this.downloadDirectoryPath = DEFAULT_DOWNLOAD_DIRECTORY;
        }
        return downloadDirectoryPath;
    }

    protected final DependencyProvider getDependencyProvider() throws IOException, ReflectiveOperationException {
        if (downloadDirectoryPath == null) {
            this.dependencyProvider = DependencyProviderFactory.createExternalDependencyProvider();
        }
        return dependencyProvider;
    }

    protected final RelocationHelperFactory getRelocationHelperFactory() {
        if (relocationHelperFactory == null) {
            final FilePathStrategy pathStrategy = FilePathStrategy.createRelocationStrategy(getDownloadDirectoryPath().toFile(), getApplicationName());
            final MetaMediatorFactory mediatorFactory = new AttributeMetaMediatorFactory();
            this.relocationHelperFactory = new VerifyingRelocationHelperFactory(pathStrategy, mediatorFactory);
        }
        return relocationHelperFactory;
    }

    protected final DependencyInjectorFactory getInjectorFactory() {
        if (injectorFactory == null) {
            this.injectorFactory = new SimpleDependencyInjectorFactory();
        }
        return injectorFactory;
    }

    protected final DependencyResolverFactory getResolverFactory() {
        if (resolverFactory == null) {
            this.resolverFactory = new CachingDependencyResolverFactory();
        }
        return resolverFactory;
    }

    protected final RepositoryEnquirerFactory getEnquirerFactory() {
        if (enquirerFactory == null) {
            final PathResolutionStrategy resolutionStrategy = new MavenPathResolutionStrategy();
            final URLPinger urlPinger = new HttpURLPinger();
            this.enquirerFactory = new PingingRepositoryEnquirerFactory(resolutionStrategy, urlPinger);
        }
        return enquirerFactory;
    }

    protected final DependencyDownloaderFactory getDownloaderFactory() {
        if (downloaderFactory == null) {
            this.downloaderFactory = new URLDependencyDownloaderFactory();
        }
        return downloaderFactory;
    }

    protected final MirrorSelector getMirrorSelector() {
        if (mirrorSelector == null) {
            mirrorSelector = new SimpleMirrorSelector();
        }
        return mirrorSelector;
    }

    protected final DependencyInjector createInjector(final DependencyDataProviderFactory dataProviderFactory) throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException {
        final DependencyProvider dependencyProvider = getDependencyProvider();
        final DependencyDataProvider dataProvider = dataProviderFactory.forFile(getDependencyFileUrl());
        final DependencyData data = dataProvider.get();
        final Collection<Repository> repositories = getMirrorSelector()
                .select(data.getRepositories(), data.getMirrors());
        final Relocator relocator = dependencyProvider.createRelocator(data.getRelocations());
        final RelocationHelper relocationHelper = getRelocationHelperFactory().create(relocator);
        final FilePathStrategy filePathStrategy = FilePathStrategy.createDefault(getDownloadDirectoryPath().toFile());
        final OutputWriterFactory outputWriterFactory = new DependencyOutputWriterFactory(filePathStrategy);
        final RepositoryEnquirerFactory enquirerFactory = getEnquirerFactory();
        final DependencyResolver resolver = getResolverFactory().create(repositories, enquirerFactory);
        final DependencyDownloader downloader = getDownloaderFactory().create(outputWriterFactory, resolver);
        return getInjectorFactory().create(downloader, relocationHelper);
    }

    public abstract Application build() throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException;
}
