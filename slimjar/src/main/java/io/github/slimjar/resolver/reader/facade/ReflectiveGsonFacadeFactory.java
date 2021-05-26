package io.github.slimjar.resolver.reader.facade;

import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.injector.loader.InjectableClassLoader;
import io.github.slimjar.injector.loader.IsolatedInjectableClassLoader;
import io.github.slimjar.relocation.PassthroughRelocator;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.util.Packages;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

public final class ReflectiveGsonFacadeFactory implements GsonFacadeFactory {
    private static final String GSON_PACKAGE = "com#google#gson#Gson";

    private final Constructor<?> gsonConstructor;
    private final Method gsonFromJsonMethod;

    private ReflectiveGsonFacadeFactory(Constructor<?> gsonConstructor, Method gsonFromJsonMethod) {
        this.gsonConstructor = gsonConstructor;
        this.gsonFromJsonMethod = gsonFromJsonMethod;
    }

    @Override
    public GsonFacade createFacade() throws ReflectiveOperationException {
        final Object gson = gsonConstructor.newInstance();
        return new ReflectiveGsonFacade(gson, gsonFromJsonMethod);
    }

    public static GsonFacadeFactory create() throws ReflectiveOperationException, NoSuchAlgorithmException, IOException, URISyntaxException {
        final InjectableClassLoader classLoader = new IsolatedInjectableClassLoader();
        return create(classLoader);
    }

    public static GsonFacadeFactory create(final InjectableClassLoader classLoader) throws ReflectiveOperationException, NoSuchAlgorithmException, IOException, URISyntaxException {
        ApplicationBuilder.injecting("SlimJar", classLoader)
                .dataProviderFactory((url) -> ReflectiveGsonFacadeFactory::getGsonDependency)
                .relocatorFactory((rules) -> new PassthroughRelocator())
                .relocationHelperFactory((relocator) -> (dependency,file) -> file)
                .build();
        final Class<?> gsonClass = Class.forName(Packages.fix(GSON_PACKAGE), true, classLoader);
        final Constructor<?> gsonConstructor = gsonClass.getConstructor();
        final Method gsonFromJsonMethod = gsonClass.getMethod("fromJson", Reader.class, Class.class);
        return new ReflectiveGsonFacadeFactory(gsonConstructor, gsonFromJsonMethod);
    }

    private static DependencyData getGsonDependency() throws MalformedURLException {
        final Dependency gson = new Dependency(
                "com.google.code.gson",
                "gson",
                "2.8.6",
                null,
                Collections.emptyList()
        );
        final Repository centralRepository = new Repository(new URL(SimpleMirrorSelector.CENTRAL_URL));
        return new DependencyData(
                Collections.emptySet(),
                Collections.singleton(centralRepository),
                Collections.singleton(gson),
                Collections.emptyList()
        );
    }
}
