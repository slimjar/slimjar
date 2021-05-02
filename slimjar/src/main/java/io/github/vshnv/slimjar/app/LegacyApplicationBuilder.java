package io.github.vshnv.slimjar.app;

import com.google.gson.Gson;
import io.github.vshnv.slimjar.data.DependencyData;
import io.github.vshnv.slimjar.resolver.reader.DependencyReader;
import io.github.vshnv.slimjar.resolver.reader.GsonDependencyReader;
import io.github.vshnv.slimjar.downloader.DependencyDownloader;
import io.github.vshnv.slimjar.downloader.output.FileOutputWriterFactory;
import io.github.vshnv.slimjar.downloader.URLDependencyDownloader;
import io.github.vshnv.slimjar.injector.DependencyInjector;
import io.github.vshnv.slimjar.injector.DownloadingDependencyInjector;
import io.github.vshnv.slimjar.injector.loader.Injectable;
import io.github.vshnv.slimjar.injector.loader.InjectableClassLoader;
import io.github.vshnv.slimjar.util.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public final class LegacyApplicationBuilder {

    private static final String userHome = System.getProperty("user.home");
    private static final String defaultPath = String.format("%s/.jar_dependencies", userHome);
    private static final File defaultDirectory = new File(defaultPath);

    private DependencyData dependencyData;
    private File dependenciesFile;
    private DependencyReader dependencyReader;
    private DependencyDownloader dependencyDownloader;
    private DependencyInjector dependencyInjector;

    public LegacyApplicationBuilder withDependencies(DependencyData dependencies) {
        this.dependencyData = dependencies;
        return this;
    }

    public LegacyApplicationBuilder withDependenciesFrom(File dependenciesFile) {
        this.dependenciesFile = dependenciesFile;
        return this;
    }

    public LegacyApplicationBuilder withDependencyReader(DependencyReader dependencyReader) {
        this.dependencyReader = dependencyReader;
        return this;
    }

    public LegacyApplicationBuilder withDependencyDownloader(DependencyDownloader dependencyDownloader) {
        this.dependencyDownloader = dependencyDownloader;
        return this;
    }

    public LegacyApplicationBuilder withDependencyInjector(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
        return this;
    }

    protected DependencyData getDependencyData() {
        if (dependencyData == null) {
            final File file = getDependenciesFile();
            final DependencyReader reader = getDependencyReader();
            try (FileInputStream inputStream = new FileInputStream(file)) {
                return reader.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dependencyData;
    }

    protected File getDependenciesFile() {
        if (dependenciesFile == null) {
            URL resourceURL = Objects.requireNonNull(
                    getClass().getClassLoader().getResource("dependencies.json"),
                    "Could not find default dependency file(dependencies.json)!"
            );
            return new File(resourceURL.getFile());
        }
        return dependenciesFile;
    }

    protected DependencyReader getDependencyReader() {
        if (dependencyReader == null) {
            return new GsonDependencyReader(new Gson());
        }
        return dependencyReader;
    }

    protected DependencyDownloader getDependencyDownloader() {
        if (dependencyDownloader == null) {
            return new URLDependencyDownloader(new FileOutputWriterFactory(defaultDirectory));
        }
        return dependencyDownloader;
    }

    protected DependencyInjector getDependencyInjector() {
        if (dependencyInjector == null) {
            return new DownloadingDependencyInjector(getDependencyDownloader());
        }
        return dependencyInjector;
    }

    @SuppressWarnings("unchecked")
    public Application buildInjectable(final String fqClazzName, final Object... args) throws ReflectiveOperationException {
        final InjectableClassLoader classLoader = new InjectableClassLoader(fqClazzName);
        injectDependenciesInto(classLoader);

        final Class<?> clazz = classLoader.loadClass(fqClazzName);
        if (!Application.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Specified class does not extend Application");
        }

        final Class<Application> applicationClass =
                (Class<Application>) Class.forName(fqClazzName, true, classLoader);
        final Class<?>[] constructorParams = Parameters.typesFrom(args);
        return applicationClass.getConstructor(constructorParams).newInstance(args);
    }

    public Application buildAppendable(final URLClassLoader appendableCL) {
        injectDependenciesInto(Injectable.wrap(appendableCL));
        return new AppendingApplication();
    }

    public Application buildAppendable() {
        return buildAppendable((URLClassLoader)getClass().getClassLoader());
    }

    private void injectDependenciesInto(Injectable injectable) {
        final DependencyData dependencyData = getDependencyData();
        final DependencyInjector dependencyInjector = getDependencyInjector();
        dependencyInjector.inject(injectable, dependencyData);
    }
}
