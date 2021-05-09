package io.github.slimjar.app;

import io.github.slimjar.downloader.output.DependencyFileOutputWriterFactory;
import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.resolver.CachingDependencyResolver;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import io.github.slimjar.resolver.enquirer.SimpleRepositoryEnquirerFactory;
import io.github.slimjar.resolver.pinger.HttpURLPinger;
import io.github.slimjar.resolver.pinger.URLPinger;
import io.github.slimjar.resolver.strategy.MavenPathResolutionStrategy;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;
import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.downloader.URLDependencyDownloader;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.injector.DownloadingDependencyInjector;
import io.github.slimjar.injector.loader.InjectableClassLoader;
import junit.framework.TestCase;

import java.io.File;
import java.util.Collections;

public class ApplicationBuilderTest extends TestCase {
/*    public void testInjectingApplicationLoader() throws ReflectiveOperationException {
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
    }*/
}
