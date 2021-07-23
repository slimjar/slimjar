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

package io.github.slimjar.resolver.reader.facade;

import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.injector.loader.InjectableClassLoader;
import io.github.slimjar.injector.loader.IsolatedInjectableClassLoader;
import io.github.slimjar.relocation.PassthroughRelocator;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.util.Packages;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;

public final class ReflectiveGsonFacadeFactory implements GsonFacadeFactory {
    private static final String GSON_PACKAGE = "com#google#gson#Gson";
    private static final String GSON_TYPES_PACKAGE = "com#google#gson#internal#$Gson$Types";

    private final Constructor<?> gsonConstructor;
    private final Method gsonFromJsonMethod;
    private final Method gsonFromJsonTypeMethod;
    private final Method canonicalizeMethod;

    private ReflectiveGsonFacadeFactory(final Constructor<?> gsonConstructor, final Method gsonFromJsonMethod, final Method gsonFromJsonTypeMethod, final Method canonicalizeMethod) {
        this.gsonConstructor = gsonConstructor;
        this.gsonFromJsonMethod = gsonFromJsonMethod;
        this.gsonFromJsonTypeMethod = gsonFromJsonTypeMethod;
        this.canonicalizeMethod = canonicalizeMethod;
    }

    @Override
    public GsonFacade createFacade() throws ReflectiveOperationException {
        final Object gson = gsonConstructor.newInstance();
        return new ReflectiveGsonFacade(gson, gsonFromJsonMethod, gsonFromJsonTypeMethod, canonicalizeMethod);
    }

    public static GsonFacadeFactory create(final Path downloadPath, final Collection<Repository> repositories) throws ReflectiveOperationException, NoSuchAlgorithmException, IOException, URISyntaxException {
        final InjectableClassLoader classLoader = new IsolatedInjectableClassLoader();
        return create(downloadPath, repositories, classLoader);
    }

    public static GsonFacadeFactory create(final Path downloadPath, final Collection<Repository> repositories, final InjectableClassLoader classLoader) throws ReflectiveOperationException, NoSuchAlgorithmException, IOException, URISyntaxException {
        ApplicationBuilder.injecting("SlimJar", classLoader)
                .downloadDirectoryPath(downloadPath)
                .dataProviderFactory((url) -> () -> ReflectiveGsonFacadeFactory.getGsonDependency(repositories))
                .relocatorFactory((rules) -> new PassthroughRelocator())
                .preResolutionDataProviderFactory(a -> Collections::emptyMap)
                .relocationHelperFactory((relocator) -> (dependency,file) -> file)
                .build();
        final Class<?> gsonClass = Class.forName(Packages.fix(GSON_PACKAGE), true, classLoader);
        final Constructor<?> gsonConstructor = gsonClass.getConstructor();
        final Method gsonFromJsonMethod = gsonClass.getMethod("fromJson", Reader.class, Class.class);
        final Method gsonFromJsonTypeMethod = gsonClass.getMethod("fromJson", Reader.class, Type.class);

        final Class<?> gsonTypesClass = Class.forName(Packages.fix(GSON_TYPES_PACKAGE), true, classLoader);

        final Method canonicalizeMethod = gsonTypesClass.getMethod("canonicalize", Type.class);
        return new ReflectiveGsonFacadeFactory(gsonConstructor, gsonFromJsonMethod, gsonFromJsonTypeMethod, canonicalizeMethod);
    }

    private static DependencyData getGsonDependency(final Collection<Repository> repositories) throws MalformedURLException {
        final Dependency gson = new Dependency(
                Packages.fix("com#google#code#gson"),
                "gson",
                "2.8.6",
                null,
                Collections.emptyList()
        );
        return new DependencyData(
                Collections.emptySet(),
                repositories,
                Collections.singleton(gson),
                Collections.emptyList()
        );
    }
}
