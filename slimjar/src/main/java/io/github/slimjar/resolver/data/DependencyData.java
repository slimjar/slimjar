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

package io.github.slimjar.resolver.data;

import io.github.slimjar.relocation.RelocationRule;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public final class DependencyData {

    private final Collection<Mirror> mirrors;
    private final Collection<Repository> repositories;
    private final Collection<Dependency> dependencies;
    private final Collection<RelocationRule> relocations;

    public DependencyData(
            final Collection<Mirror> mirrors,
            final Collection<Repository> repositories,
            final Collection<Dependency> dependencies,
            final Collection<RelocationRule> relocations
    ) {
        this.mirrors = Collections.unmodifiableCollection(mirrors);
        this.repositories = Collections.unmodifiableCollection(repositories);
        this.dependencies = Collections.unmodifiableCollection(dependencies);
        this.relocations = Collections.unmodifiableCollection(relocations);
    }

    public Collection<Repository> getRepositories() {
        return repositories;
    }

    public Collection<Dependency> getDependencies() {
        return dependencies;
    }

    public Collection<RelocationRule> getRelocations() {
        return relocations;
    }

    public Collection<Mirror> getMirrors() {
        return mirrors;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DependencyData that = (DependencyData) o;
        return isCollectionEqual(repositories, that.repositories) && isCollectionEqual(dependencies, that.dependencies) && isCollectionEqual(relocations, that.relocations);
    }

    private <T> boolean isCollectionEqual(Collection<T> a, Collection<T> b) {
        return a.containsAll(b) && b.containsAll(a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repositories, dependencies, relocations);
    }

    @Override
    public String toString() {
        return "DependencyData{" +
                "mirrors=" + mirrors +
                ", repositories=" + repositories +
                ", dependencies=" + dependencies +
                ", relocations=" + relocations +
                '}';
    }
}
