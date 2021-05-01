package io.github.vshnv.slimjar.relocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class RelocationRule {
    private final String originalPackagePattern;
    private final String relocatedPackagePattern;
    private final Collection<String> exclusions;
    private final Collection<String> inclusions;

    public RelocationRule(String original, String relocated, Collection<String> exclusions, Collection<String> inclusions) {
        this.originalPackagePattern = original;
        this.relocatedPackagePattern = relocated;
        this.exclusions = exclusions;
        this.inclusions = inclusions;
    }

    public RelocationRule(String original, String relocated) {
        this(original, relocated, Collections.emptyList(), Collections.emptyList());
    }

    public String getOriginalPackagePattern() {
        return originalPackagePattern;
    }

    public String getRelocatedPackagePattern() {
        return relocatedPackagePattern;
    }

    public Collection<String> getExclusions() {
        return exclusions;
    }

    public Collection<String> getInclusions() {
        return inclusions;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RelocationRule that = (RelocationRule) o;
        return originalPackagePattern.equals(that.originalPackagePattern) &&
                relocatedPackagePattern.equals(that.relocatedPackagePattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalPackagePattern, relocatedPackagePattern);
    }

    @Override
    public String toString() {
        return "RelocationRule{" +
                "originalPackagePattern='" + originalPackagePattern + '\'' +
                ", relocatedPackagePattern='" + relocatedPackagePattern + '\'' +
                '}';
    }
}
