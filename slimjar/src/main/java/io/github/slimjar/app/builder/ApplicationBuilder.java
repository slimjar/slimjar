package io.github.slimjar.app.builder;

import io.github.slimjar.app.Application;
import io.github.slimjar.downloader.DependencyDownloaderFactory;
import io.github.slimjar.downloader.URLDependencyDownloaderFactory;
import io.github.slimjar.downloader.output.DependencyOutputWriterFactory;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.downloader.strategy.ChecksumFilePathStrategy;
import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.downloader.verify.*;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.injector.DependencyInjectorFactory;
import io.github.slimjar.injector.SimpleDependencyInjectorFactory;
import io.github.slimjar.injector.helper.InjectionHelperFactory;
import io.github.slimjar.injector.loader.Injectable;
import io.github.slimjar.injector.loader.WrappedInjectableClassLoader;
import io.github.slimjar.relocation.JarFileRelocatorFactory;
import io.github.slimjar.relocation.RelocatorFactory;
import io.github.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import io.github.slimjar.relocation.helper.RelocationHelperFactory;
import io.github.slimjar.relocation.helper.VerifyingRelocationHelperFactory;
import io.github.slimjar.relocation.meta.AttributeMetaMediatorFactory;
import io.github.slimjar.relocation.meta.MetaMediatorFactory;
import io.github.slimjar.resolver.CachingDependencyResolverFactory;
import io.github.slimjar.resolver.DependencyResolverFactory;
import io.github.slimjar.resolver.enquirer.PingingRepositoryEnquirerFactory;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import io.github.slimjar.resolver.mirrors.MirrorSelector;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.pinger.HttpURLPinger;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;
import io.github.slimjar.resolver.reader.ExternalDependencyDataProviderFactory;
import io.github.slimjar.resolver.reader.GsonDependencyDataProviderFactory;
import io.github.slimjar.resolver.reader.facade.GsonFacadeFactory;
import io.github.slimjar.resolver.reader.facade.ReflectiveGsonFacadeFactory;
import io.github.slimjar.resolver.strategy.MavenChecksumPathResolutionStrategy;
import io.github.slimjar.resolver.strategy.MavenPathResolutionStrategy;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
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
    private RelocatorFactory relocatorFactory;
    private DependencyDataProviderFactory moduleDataProviderFactory;
    private DependencyDataProviderFactory dataProviderFactory;
    private RelocationHelperFactory relocationHelperFactory;
    private DependencyInjectorFactory injectorFactory;
    private DependencyResolverFactory resolverFactory;
    private RepositoryEnquirerFactory enquirerFactory;
    private DependencyDownloaderFactory downloaderFactory;
    private DependencyVerifierFactory verifierFactory;
    private MirrorSelector mirrorSelector;

    protected ApplicationBuilder(final String applicationName) {
        this.applicationName = Objects.requireNonNull(applicationName, "Requires non-null application name!");
    }

    public static ApplicationBuilder isolated(final String name, final IsolationConfiguration config, Object[] args) {
        return new IsolatedApplicationBuilder(name, config, args);
    }

    public static ApplicationBuilder appending(final String name, final URLClassLoader classLoader) {
        return injecting(name, new WrappedInjectableClassLoader(classLoader));
    }

    public static ApplicationBuilder injecting(final String name, final Injectable injectable) {
        return new InjectingApplicationBuilder(name, injectable);
    }

    public final ApplicationBuilder dependencyFileUrl(final URL dependencyFileUrl) {
        this.dependencyFileUrl = dependencyFileUrl;
        return this;
    }

    public final ApplicationBuilder downloadDirectoryPath(final Path downloadDirectoryPath) {
        this.downloadDirectoryPath = downloadDirectoryPath;
        return this;
    }

    public final ApplicationBuilder relocatorFactory(final RelocatorFactory relocatorFactory) {
        this.relocatorFactory = relocatorFactory;
        return this;
    }

    public final ApplicationBuilder moduleDataProviderFactory(final DependencyDataProviderFactory moduleDataProviderFactory) {
        this.moduleDataProviderFactory = moduleDataProviderFactory;
        return this;
    }

    public final ApplicationBuilder dataProviderFactory(final DependencyDataProviderFactory dataProviderFactory) {
        this.dataProviderFactory = dataProviderFactory;
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

    public final ApplicationBuilder verifierFactory(final DependencyVerifierFactory verifierFactory) {
        this.verifierFactory = verifierFactory;
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
            this.dependencyFileUrl = getClass().getClassLoader().getResource("slimjar.json");
        }
        return dependencyFileUrl;
    }

    protected final Path getDownloadDirectoryPath() {
        if (downloadDirectoryPath == null) {
            this.downloadDirectoryPath = DEFAULT_DOWNLOAD_DIRECTORY;
        }
        return downloadDirectoryPath;
    }

    protected final RelocatorFactory getRelocatorFactory() throws ReflectiveOperationException, NoSuchAlgorithmException, IOException, URISyntaxException {
        if (relocatorFactory == null) {
            final JarRelocatorFacadeFactory jarRelocatorFacadeFactory = ReflectiveJarRelocatorFacadeFactory.create();
            this.relocatorFactory = new JarFileRelocatorFactory(jarRelocatorFacadeFactory);
        }
        return relocatorFactory;
    }

    protected final DependencyDataProviderFactory getModuleDataProviderFactory() throws URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException, IOException {
        if (moduleDataProviderFactory == null) {
            final GsonFacadeFactory gsonFacadeFactory = ReflectiveGsonFacadeFactory.create();
            this.moduleDataProviderFactory = new ExternalDependencyDataProviderFactory(gsonFacadeFactory);
        }
        return moduleDataProviderFactory;
    }

    protected final DependencyDataProviderFactory getDataProviderFactory() throws URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException, IOException {
        if (dataProviderFactory == null) {
            final GsonFacadeFactory gsonFacadeFactory = ReflectiveGsonFacadeFactory.create();
            this.dataProviderFactory = new GsonDependencyDataProviderFactory(gsonFacadeFactory);
        }
        return dataProviderFactory;
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
            final PathResolutionStrategy checksumResolutionStrategy = new MavenChecksumPathResolutionStrategy("SHA-1");
            final URLPinger urlPinger = new HttpURLPinger();
            this.enquirerFactory = new PingingRepositoryEnquirerFactory(resolutionStrategy, checksumResolutionStrategy, urlPinger);
        }
        return enquirerFactory;
    }

    protected final DependencyDownloaderFactory getDownloaderFactory() {
        if (downloaderFactory == null) {
            this.downloaderFactory = new URLDependencyDownloaderFactory();
        }
        return downloaderFactory;
    }

    protected final DependencyVerifierFactory getVerifierFactory() throws NoSuchAlgorithmException {
        if (verifierFactory == null) {
            final FilePathStrategy filePathStrategy = ChecksumFilePathStrategy.createStrategy(getDownloadDirectoryPath().toFile(), "SHA-1");
            final OutputWriterFactory checksumOutputFactory = new DependencyOutputWriterFactory(filePathStrategy);
            final DependencyVerifierFactory fallback = new PassthroughDependencyVerifierFactory();
            final ChecksumCalculator checksumCalculator = new FileChecksumCalculator("SHA-1");
            this.verifierFactory = new ChecksumDependencyVerifierFactory(checksumOutputFactory, fallback, checksumCalculator);
        }
        return verifierFactory;
    }

    protected final MirrorSelector getMirrorSelector() {
        if (mirrorSelector == null) {
            mirrorSelector = new SimpleMirrorSelector();
        }
        return mirrorSelector;
    }

    protected final DependencyInjector createInjector() throws IOException, URISyntaxException, NoSuchAlgorithmException, ReflectiveOperationException {
        final InjectionHelperFactory injectionHelperFactory = new InjectionHelperFactory(
                getDownloadDirectoryPath(),
                getRelocatorFactory(),
                getDataProviderFactory(),
                getRelocationHelperFactory(),
                getInjectorFactory(),
                getResolverFactory(),
                getEnquirerFactory(),
                getDownloaderFactory(),
                getVerifierFactory(),
                getMirrorSelector()
        );
        return getInjectorFactory().create(injectionHelperFactory);
    }

    public abstract Application build() throws IOException, ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException;
}
