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

package io.github.slimjar.resolver.enquirer;

import io.github.slimjar.logging.LogDispatcher;
import io.github.slimjar.logging.ProcessLogger;
import io.github.slimjar.resolver.ResolutionResult;
import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.strategy.PathResolutionStrategy;
import io.github.slimjar.resolver.pinger.URLPinger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public final class PingingRepositoryEnquirer implements RepositoryEnquirer {
    private static final ProcessLogger LOGGER = LogDispatcher.getMediatingLogger();
    private final Repository repository;
    private final PathResolutionStrategy dependencyURLCreationStrategy;
    private final PathResolutionStrategy checksumURLCreationStrategy;
    private final PathResolutionStrategy pomURLCreationStrategy;
    private final URLPinger urlPinger;

    public PingingRepositoryEnquirer(final Repository repository, final PathResolutionStrategy urlCreationStrategy, final PathResolutionStrategy checksumURLCreationStrategy, final PathResolutionStrategy pomURLCreationStrategy, final URLPinger urlPinger) {
        this.repository = repository;
        this.dependencyURLCreationStrategy = urlCreationStrategy;
        this.checksumURLCreationStrategy = checksumURLCreationStrategy;
        this.pomURLCreationStrategy = pomURLCreationStrategy;
        this.urlPinger = urlPinger;
    }

    @Override
    public ResolutionResult enquire(final Dependency dependency) {
        LOGGER.debug("Enquiring repositories to find {0}", dependency.getArtifactId());
        final Optional<URL> resolvedDependency = dependencyURLCreationStrategy.pathTo(repository, dependency)
                .stream().map((path) -> {
                    try {
                        return new URL(path);
                    } catch (MalformedURLException e) {
                        return null;
                    }
                }).filter(urlPinger::ping)
                .findFirst();
        if (!resolvedDependency.isPresent()) {
            return pomURLCreationStrategy.pathTo(repository, dependency).stream().map((path) -> {
                try {
                    return new URL(path);
                } catch (MalformedURLException e) {
                    return null;
                }
            }).filter(urlPinger::ping)
                    .findFirst()
                    .map(url -> new ResolutionResult(repository, null, null, true))
                    .orElse(null);
        }
        final Optional<URL> resolvedChecksum = checksumURLCreationStrategy.pathTo(repository, dependency)
                .stream().map((path) -> {
                    try {
                        return new URL(path);
                    } catch (MalformedURLException e) {
                        return null;
                    }
                }).filter(urlPinger::ping)
                .findFirst();
        return new ResolutionResult(repository, resolvedDependency.get(), resolvedChecksum.orElse(null), false);
    }
}
