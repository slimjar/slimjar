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

package io.github.slimjar.resolver.strategy;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

public final class MavenChecksumPathResolutionStrategy implements PathResolutionStrategy {
    private final PathResolutionStrategy resolutionStrategy;
    private final String algorithm;

    public MavenChecksumPathResolutionStrategy(final String algorithm, final PathResolutionStrategy resolutionStrategy) {
        this.algorithm = algorithm.replaceAll("[ -]", "").toLowerCase(Locale.ENGLISH);
        this.resolutionStrategy = resolutionStrategy;
    }

    @Override
    public Collection<String> pathTo(final Repository repository, final Dependency dependency) {
        return resolutionStrategy.pathTo(repository, dependency).stream().map(path -> path + "." + algorithm).collect(Collectors.toSet());
    }
}
