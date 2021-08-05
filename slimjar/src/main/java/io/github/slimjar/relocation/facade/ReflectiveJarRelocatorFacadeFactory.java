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

package io.github.slimjar.relocation.facade;

import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.injector.loader.InjectableClassLoader;
import io.github.slimjar.injector.loader.IsolatedInjectableClassLoader;
import io.github.slimjar.relocation.PassthroughRelocator;
import io.github.slimjar.relocation.RelocationRule;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.util.Packages;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public final class ReflectiveJarRelocatorFacadeFactory implements JarRelocatorFacadeFactory {
    private static final String JAR_RELOCATOR_PACKAGE = "me#lucko#jarrelocator#JarRelocator";
    private static final String RELOCATION_PACKAGE = "me#lucko#jarrelocator#Relocation";

    private final Constructor<?> jarRelocatorConstructor;
    private final Constructor<?> relocationConstructor;
    private final Method jarRelocatorRunMethod;

    private ReflectiveJarRelocatorFacadeFactory(final Constructor<?> jarRelocatorConstructor, final Constructor<?> relocationConstructor, final Method jarRelocatorRunMethod) {
        this.jarRelocatorConstructor = jarRelocatorConstructor;
        this.relocationConstructor = relocationConstructor;
        this.jarRelocatorRunMethod = jarRelocatorRunMethod;
    }


    @Override
    public JarRelocatorFacade createFacade(final File input, final File output, final Collection<RelocationRule> relocationRules) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        final Collection<Object> relocations = new HashSet<>();
        for (final RelocationRule rule: relocationRules) {
            relocations.add(createRelocation(relocationConstructor, rule));
        }
        final Object relocator = createRelocator(jarRelocatorConstructor, input, output, relocations);
        return new ReflectiveJarRelocatorFacade(relocator, jarRelocatorRunMethod);
    }

    private static Object createRelocation(final Constructor<?> relocationConstructor, final RelocationRule rule) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return relocationConstructor.newInstance(rule.getOriginalPackagePattern(), rule.getRelocatedPackagePattern(), rule.getExclusions(), rule.getInclusions());
    }

    private static Object createRelocator(final Constructor<?> jarRelocatorConstructor, final File input, final File output, final Collection<Object> rules) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return jarRelocatorConstructor.newInstance(input, output, rules);
    }

    private static DependencyData getJarRelocatorDependency(final Collection<Repository> repositories) throws MalformedURLException {
        final Dependency asm = new Dependency(
                Packages.fix("org#ow2#asm"),
                "asm",
                "9.1",
                null,
                Collections.emptyList()
        );
        final Dependency asmCommons = new Dependency(
                Packages.fix("org#ow2#asm"),
                "asm-commons",
                "9.1",
                null,
                Collections.emptyList()
        );
        final Dependency jarRelocator = new Dependency(
                Packages.fix("me#lucko"),
                "jar-relocator",
                "1.4",
                null,
                Arrays.asList(asm, asmCommons)
        );
        return new DependencyData(
                Collections.emptySet(),
                repositories,
                Collections.singleton(jarRelocator),
                Collections.emptyList()
        );
    }

    public static JarRelocatorFacadeFactory create(final Path downloadPath, final Collection<Repository> repositories) throws URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException, IOException {
        final InjectableClassLoader classLoader = new IsolatedInjectableClassLoader();
        return create(downloadPath, repositories, classLoader);
    }

    public static JarRelocatorFacadeFactory create(final Path downloadPath, final Collection<Repository> repositories, final InjectableClassLoader classLoader) throws URISyntaxException, ReflectiveOperationException, NoSuchAlgorithmException, IOException {
        ApplicationBuilder.injecting("SlimJar", classLoader)
                .downloadDirectoryPath(downloadPath)
                .preResolutionDataProviderFactory(a -> Collections::emptyMap)
                .dataProviderFactory((url) -> () -> ReflectiveJarRelocatorFacadeFactory.getJarRelocatorDependency(repositories))
                .relocatorFactory((rules) -> new PassthroughRelocator())
                .relocationHelperFactory((relocator) -> (dependency,file) -> file)
                .build();
        final Class<?> jarRelocatorClass = Class.forName(Packages.fix(JAR_RELOCATOR_PACKAGE), true, classLoader);
        final Class<?> relocationClass = Class.forName(Packages.fix(RELOCATION_PACKAGE), true, classLoader);
        final Constructor<?> jarRelocatorConstructor = jarRelocatorClass.getConstructor(File.class, File.class, Collection.class);
        final Constructor<?> relocationConstructor = relocationClass.getConstructor(String.class, String.class, Collection.class, Collection.class);
        final Method runMethod = jarRelocatorClass.getMethod("run");
        return new ReflectiveJarRelocatorFacadeFactory(jarRelocatorConstructor, relocationConstructor, runMethod);
    }
}
