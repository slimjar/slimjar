package io.github.slimjar.app.external;

import io.github.slimjar.app.ApplicationConfiguration;
import io.github.slimjar.app.module.ModuleExtractor;
import io.github.slimjar.app.module.TemporaryModuleExtractor;
import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.downloader.URLDependencyDownloader;
import io.github.slimjar.downloader.output.DependencyOutputWriterFactory;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.injector.DownloadingDependencyInjector;
import io.github.slimjar.injector.loader.InjectableClassLoader;
import io.github.slimjar.injector.loader.IsolatedInjectableClassLoader;
import io.github.slimjar.relocation.PassthroughRelocator;
import io.github.slimjar.resolver.CachingDependencyResolver;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.enquirer.SimpleRepositoryEnquirerFactory;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.pinger.HttpURLPinger;
import io.github.slimjar.resolver.strategy.MavenPathResolutionStrategy;
import io.github.slimjar.util.Modules;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class DependencyProviderFactory {
    private DependencyProviderFactory() {

    }
    public static DependencyProvider createExternalDependencyProvider() throws IOException, ReflectiveOperationException {
        final FilePathStrategy filePathStrategy = FilePathStrategy.createDefault(ApplicationConfiguration.DEFAULT_DOWNLOAD_DIRECTORY);
        final OutputWriterFactory outputWriterFactory = new DependencyOutputWriterFactory(filePathStrategy, filePathStrategy, new PassthroughRelocator());
        final DependencyResolver resolver = new CachingDependencyResolver(Collections.singleton(new Repository(new URL(SimpleMirrorSelector.CENTRAL_URL))), new SimpleRepositoryEnquirerFactory(new MavenPathResolutionStrategy(), new HttpURLPinger()));
        final DependencyDownloader downloader = new URLDependencyDownloader(outputWriterFactory, resolver);
        final DependencyInjector injector = new DownloadingDependencyInjector(downloader);
        final URL isolatedModule = Modules.findModule("slimjar-external");
        final ModuleExtractor moduleExtractor = new TemporaryModuleExtractor();
        final URL tempModuleURL = moduleExtractor.extractModule(isolatedModule, "slimjar-external");
        final InjectableClassLoader injectableClassLoader = new IsolatedInjectableClassLoader(new URL[]{tempModuleURL}, IsolatedInjectableClassLoader.class.getClassLoader(), Collections.emptyList());
        injector.inject(injectableClassLoader, getSelfDependencies());
        final String fqClassName = "io.github.slimjar.external.ExternalDependencyProvider";
        final Class<DependencyProvider> providerClass = (Class<DependencyProvider>) Class.forName(fqClassName, true, injectableClassLoader);
        return providerClass.newInstance();
    }


    private static Collection<Dependency> getSelfDependencies() {
        final Dependency asm = new Dependency(
                "org.ow2.asm",
                "asm",
                "7.1",
                null,
                Collections.emptyList()
        );
        final Dependency asmCommons = new Dependency(
                "org.ow2.asm",
                "asm-commons",
                "7.1",
                null,
                Collections.emptyList()
        );
        final Dependency jarRelocator = new Dependency(
                "me.lucko",
                "jar-relocator",
                "1.4",
                null,
                Arrays.asList(asm, asmCommons)
        );
        final Dependency gson = new Dependency(
                "com.google.code.gson",
                "gson",
                "2.8.6",
                null,
                Collections.emptyList()
        );
        return Arrays.asList(gson, jarRelocator);
    }
}
