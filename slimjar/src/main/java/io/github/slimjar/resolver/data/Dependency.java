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

import java.util.Collection;
import java.util.Objects;

public final class Dependency {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String snapshotId;
    private final Collection<Dependency> transitive;

    public Dependency(String groupId, String artifactId, String version, String snapshotId, Collection<Dependency> transitive) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.snapshotId = snapshotId;
        this.transitive = transitive;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public Collection<Dependency> getTransitive() {
        return transitive;
    }

    @Override
    public String toString() {
        final String snapshotId = getSnapshotId();
        final String suffix = (snapshotId != null && snapshotId.length() > 0) ? (":" + snapshotId): "";
        return getGroupId() + ":" + getArtifactId() + ":" + getVersion() + suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return groupId.equals(that.groupId) &&
                artifactId.equals(that.artifactId) &&
                version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
    }

}
