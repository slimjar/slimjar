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

package io.github.slimjar.injector.loader;

import java.net.URL;
import java.util.*;

public final class IsolatedInjectableClassLoader extends InjectableClassLoader {
    private final Map<String, Class<?>> delegatesMap = new HashMap<>();

    public IsolatedInjectableClassLoader() {
        this(new URL[0]);
    }

    public IsolatedInjectableClassLoader(final URL... urls) {
        this(urls, Collections.emptySet());
    }

    public IsolatedInjectableClassLoader(final URL[] urls, final Collection<Class<?>> delegates) {
      this(urls, getSystemClassLoader().getParent(), delegates);
    }

    public IsolatedInjectableClassLoader(final URL[] urls, final ClassLoader parent, final Collection<Class<?>> delegates) {
        super(urls, parent);
        for (Class<?> clazz : delegates) {
            delegatesMap.put(clazz.getName(), clazz);
        }
    }


    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // Check if class was already loaded
        final Class<?> loaded = findLoadedClass(name);
        if (loaded != null) {
            return loaded;
        }
        // Check if its any of provided classes
        final Class<?> delegate = delegatesMap.get(name);
        if (delegate != null) {
            return delegate;
        }

        // Load from 'URLClassPath's
        return super.loadClass(name, resolve);
    }

}
