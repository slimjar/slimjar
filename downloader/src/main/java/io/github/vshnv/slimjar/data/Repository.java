package io.github.vshnv.slimjar.data;

import java.util.Objects;

public class Repository {
    private final String id;
    private final String name;
    private final String url;

    public Repository(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        return id.equals(that.id) &&
                Objects.equals(name, that.name) &&
                url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url);
    }
}
