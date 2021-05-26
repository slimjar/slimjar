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

package io.github.slimjar.resolver;


import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirer;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public final class CachingDependencyResolver implements DependencyResolver {
    private final Collection<RepositoryEnquirer> repositories;
    private final Map<Dependency, ResolutionResult> cachedResults = new HashMap<>();

    public CachingDependencyResolver(final Collection<Repository> repositories, final RepositoryEnquirerFactory enquirerFactory) {
        this.repositories = repositories.stream()
                .map(enquirerFactory::create)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<ResolutionResult> resolve(final Dependency dependency) {
        return Optional.ofNullable(cachedResults.computeIfAbsent(dependency, this::attemptResolve));
    }

    private ResolutionResult attemptResolve(final Dependency dependency) {
        final Optional<ResolutionResult> result = repositories.stream().parallel()
                .map(enquirer -> enquirer.enquire(dependency))
                .filter(Objects::nonNull)
                .findFirst();
        return result.orElse(null);
    }
}
