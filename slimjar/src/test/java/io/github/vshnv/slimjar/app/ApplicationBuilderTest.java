package io.github.vshnv.slimjar.app;

import io.github.vshnv.slimjar.downloader.DependencyDownloader;
import io.github.vshnv.slimjar.downloader.URLDependencyDownloader;
import io.github.vshnv.slimjar.downloader.output.DependencyFileOutputWriterFactory;
import io.github.vshnv.slimjar.downloader.output.OutputWriterFactory;
import io.github.vshnv.slimjar.downloader.strategy.FilePathStrategy;
import io.github.vshnv.slimjar.injector.DependencyInjector;
import io.github.vshnv.slimjar.injector.DownloadingDependencyInjector;
import io.github.vshnv.slimjar.injector.loader.InjectableClassLoader;
import io.github.vshnv.slimjar.resolver.CachingDependencyResolver;
import io.github.vshnv.slimjar.resolver.DependencyResolver;
import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.DependencyData;
import io.github.vshnv.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import io.github.vshnv.slimjar.resolver.enquirer.SimpleRepositoryEnquirerFactory;
import io.github.vshnv.slimjar.resolver.pinger.HttpURLPinger;
import io.github.vshnv.slimjar.resolver.pinger.URLPinger;
import io.github.vshnv.slimjar.resolver.strategy.MavenPathResolutionStrategy;
import io.github.vshnv.slimjar.resolver.strategy.PathResolutionStrategy;
import junit.framework.TestCase;

import java.io.File;
import java.util.Collections;

public class ApplicationBuilderTest extends TestCase {
    public void testInjectingApplicationLoader() throws ReflectiveOperationException {
        final FilePathStrategy filePathStrategy = new FilePathStrategy() {
            @Override
            public File selectFileFor(Dependency dependency) {
                return null;
            }
        };
        final OutputWriterFactory outputWriterFactory = new DependencyFileOutputWriterFactory(filePathStrategy, relocationFilePathStraegy, relocator);
        final PathResolutionStrategy pathResolutionStrategy = new MavenPathResolutionStrategy();
        final URLPinger urlPinger = new HttpURLPinger();
        final RepositoryEnquirerFactory repositoryEnquirerFactory = new SimpleRepositoryEnquirerFactory(pathResolutionStrategy, urlPinger);
        final DependencyResolver dependencyResolver = new CachingDependencyResolver(Collections.emptyList(), repositoryEnquirerFactory);
        final DependencyDownloader dependencyDownloader = new URLDependencyDownloader(outputWriterFactory, dependencyResolver);
        final DependencyInjector dependencyInjector = new DownloadingDependencyInjector(dependencyDownloader);
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(
                dependencyInjector,
                () -> new DependencyData(Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet())
        );
        Application app = new ApplicationFactory(applicationConfiguration)
                .createIsolatedApplication("io.github.vshnv.slimjar.app.DummyApplication");
        assertNotNull(app);
        assertEquals(app.getClass().getClassLoader().getClass(), InjectableClassLoader.class);
    }
}
