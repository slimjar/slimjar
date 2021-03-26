package io.github.vshnv.slimjar.data;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class Dependency {
    private final String groupId;
    private final String artifactId;
    private final String version;

    public Dependency(final String groupId, final String artifactId, final String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Dependency that = (Dependency) o;
        return groupId.equals(that.groupId) &&
                artifactId.equals(that.artifactId) &&
                version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
    }

    public static Dependency fromMap(final Map<String, String> attributes) {
        final String groupId = attributes.get("groupId");
        final String attributeId = attributes.get("attributeId");
        final String version = attributes.get("version");
        if (groupId == null || attributeId == null || version == null) {
            return null;
        }
        return new Dependency(groupId, attributeId, version);
    }
}
