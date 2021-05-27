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

package io.github.slimjar.app.builder;

import io.github.slimjar.app.module.ModuleExtractor;
import io.github.slimjar.app.module.TemporaryModuleExtractor;
import io.github.slimjar.util.Modules;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public final class IsolationConfiguration {
    private final String applicationClass;
    private final Collection<String> modules;
    private final ClassLoader parentClassloader;
    private final ModuleExtractor moduleExtractor;

    public IsolationConfiguration(String applicationClass, Collection<String> modules, ClassLoader parentClassloader, ModuleExtractor moduleExtractor) {
        this.applicationClass = applicationClass;
        this.modules = Collections.unmodifiableCollection(modules);
        this.parentClassloader = parentClassloader;
        this.moduleExtractor = moduleExtractor;
    }

    public String getApplicationClass() {
        return applicationClass;
    }

    public Collection<String> getModules() {
        return modules;
    }

    public ClassLoader getParentClassloader() {
        return parentClassloader;
    }

    public ModuleExtractor getModuleExtractor() {
        return moduleExtractor;
    }

    public static Builder builder(final String applicationClass) {
        return new Builder().applicationClass(applicationClass);
    }

    public static final class Builder {
        private String applicationClass;
        private Collection<String> modules = new HashSet<>();
        private ClassLoader parentClassloader;
        private ModuleExtractor moduleExtractor;

        public Builder applicationClass(final String applicationClass) {
            this.applicationClass = applicationClass;
            return this;
        }

        public Builder modules(final Collection<String> modules) {
            final Collection<String> mod = new HashSet<>(modules);
            mod.addAll(modules);
            this.modules = mod;
            return this;
        }

        public Builder module(final String module) {
            this.modules.add(module);
            return this;
        }

        public Builder parentClassLoader(final ClassLoader classLoader) {
            this.parentClassloader = classLoader;
            return this;
        }

        public Builder moduleExtractor(final ModuleExtractor moduleExtractor) {
            this.moduleExtractor = moduleExtractor;
            return this;
        }

        String getApplicationClass() {
            if (applicationClass == null) {
                throw new AssertionError("Application Class not Provided!");
            }
            return applicationClass;
        }

        Collection<String> getModules() throws IOException, URISyntaxException {
            if (modules == null || modules.isEmpty()) {
                this.modules = Modules.findLocalModules();
            }
            return modules;
        }

        ClassLoader getParentClassloader() {
            if (parentClassloader == null) {
                this.parentClassloader = ClassLoader.getSystemClassLoader().getParent();
            }
            return parentClassloader;
        }

        ModuleExtractor getModuleExtractor() {
            if (moduleExtractor == null) {
                this.moduleExtractor = new TemporaryModuleExtractor();
            }
            return moduleExtractor;
        }

        public IsolationConfiguration build() throws IOException, URISyntaxException {
            return new IsolationConfiguration(getApplicationClass(), getModules(), getParentClassloader(), getModuleExtractor());
        }
    }
}
