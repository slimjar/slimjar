package io.github.vshnv.slimjar;

public class Dependency {
    private final String name;
    private final String url;

    public Dependency(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
