package io.github.vshnv.slimjar.resolver.data;

import java.util.Objects;

public final class Repository {
    private final String identifier;
    private final String url;

    public Repository(String identifier, String url) {
        this.identifier = identifier;
        this.url = url;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        return identifier.equals(that.identifier) &&
                url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, url);
    }

    @Override
    public String toString() {
        return "Repository{" +
                "identifier='" + identifier + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
