package io.github.slimjar.resolver;

import java.net.URL;
import java.util.Objects;

public final class ResolutionResult {
    private final URL dependencyURL;
    private final URL checksumURL;

    public ResolutionResult(final URL dependencyURL, final URL checksumURL) {
        this.dependencyURL = Objects.requireNonNull(dependencyURL);
        this.checksumURL = checksumURL;
    }

    public URL getDependencyURL() {
        return dependencyURL;
    }

    public URL getChecksumURL() {
        return checksumURL;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResolutionResult that = (ResolutionResult) o;
        return dependencyURL.equals(that.dependencyURL) &&
                Objects.equals(checksumURL, that.checksumURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencyURL, checksumURL);
    }
}
