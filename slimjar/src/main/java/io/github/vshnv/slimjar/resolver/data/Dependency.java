package io.github.vshnv.slimjar.resolver.data;

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

    public Collection<Dependency> getTransitive() {
        return transitive;
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", transitive=" + transitive +
                '}';
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

    @Override
    public String toString() {
        return "Dependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
