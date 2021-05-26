package io.github.slimjar.resolver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public final class ResolutionResult {
    private final URI dependencyURI;
    private final URI checksumURI;

    public ResolutionResult(final URI dependencyURI, final URI checksumURI) {
        this.dependencyURI = Objects.requireNonNull(dependencyURI);
        this.checksumURI = checksumURI;
    }

    public URL getDependencyURL() throws MalformedURLException {
        return dependencyURI.toURL();
    }

    public URL getChecksumURL() throws MalformedURLException {
        return checksumURI.toURL();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResolutionResult that = (ResolutionResult) o;
        return dependencyURI.equals(that.dependencyURI) &&
                Objects.equals(checksumURI, that.checksumURI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencyURI, checksumURI);
    }
}
