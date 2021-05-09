package io.github.slimjar.resolver.data;

import java.net.URL;
import java.util.Objects;

public final class Repository {

    private final URL url;

    public Repository(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "Repository{" +
                ", url='" + url + '\'' +
                '}';
    }

}
