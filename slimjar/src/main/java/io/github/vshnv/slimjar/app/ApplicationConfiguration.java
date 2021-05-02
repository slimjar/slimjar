package io.github.vshnv.slimjar.app;

import com.google.gson.Gson;
import io.github.vshnv.slimjar.downloader.DependencyDownloader;
import io.github.vshnv.slimjar.downloader.URLDependencyDownloader;
import io.github.vshnv.slimjar.downloader.output.DependencyFileOutputWriterFactory;
import io.github.vshnv.slimjar.downloader.output.OutputWriterFactory;
import io.github.vshnv.slimjar.injector.DownloadingDependencyInjector;
import io.github.vshnv.slimjar.resolver.reader.DependencyDataProvider;
import io.github.vshnv.slimjar.downloader.path.FilePathStrategy;
import io.github.vshnv.slimjar.injector.DependencyInjector;
import io.github.vshnv.slimjar.resolver.reader.DependencyReader;
import io.github.vshnv.slimjar.resolver.reader.FileDependencyDataProvider;
import io.github.vshnv.slimjar.resolver.reader.GsonDependencyReader;

import java.io.File;

public final class ApplicationConfiguration {
    private static final File DEFAULT_DOWNLOAD_DIRECTORY;

    static {
        final String userHome = System.getProperty("user.home");
        final String defaultPath = String.format("%s/.slimjar", userHome);
        DEFAULT_DOWNLOAD_DIRECTORY = new File(defaultPath);
    }

    private static final Gson GSON = new Gson();
    private final DependencyInjector dependencyInjector;
    private final DependencyDataProvider dependencyDataProvider;

    public ApplicationConfiguration(DependencyInjector dependencyInjector, DependencyDataProvider dependencyDataProvider) {
        this.dependencyInjector = dependencyInjector;
        this.dependencyDataProvider = dependencyDataProvider;
    }

    public DependencyInjector getDependencyInjector() {
        return dependencyInjector;
    }

    public DependencyDataProvider getDependencyDataProvider() {
        return dependencyDataProvider;
    }

    public static ApplicationConfiguration createDefault() {
        final String depFilePath = ApplicationConfiguration.class.getClassLoader().getResource("slimjar.json").getFile();
        if (depFilePath == null) throw new IllegalStateException("Could not find generated slimjar.json! Did you use the slimjar plugin to build?");
        return createDefault(new File(depFilePath), DEFAULT_DOWNLOAD_DIRECTORY);
    }

    public static ApplicationConfiguration createDefault(final File depFile, final File downloadDirectory) {
        final FilePathStrategy filePathStrategy = FilePathStrategy.createDefault(downloadDirectory);
        final OutputWriterFactory outputWriterFactory = new DependencyFileOutputWriterFactory(filePathStrategy);
        final DependencyDownloader dependencyDownloader = new URLDependencyDownloader(outputWriterFactory);
        final DependencyInjector dependencyInjector = new DownloadingDependencyInjector(dependencyDownloader);

        final DependencyReader dependencyReader = new GsonDependencyReader(GSON);
        final DependencyDataProvider dependencyDataProvider = new FileDependencyDataProvider(dependencyReader, depFile);
        return new ApplicationConfiguration(dependencyInjector, dependencyDataProvider);
    }


}
