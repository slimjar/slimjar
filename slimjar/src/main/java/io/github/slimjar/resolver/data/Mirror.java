package io.github.slimjar.resolver.data;

import java.net.URL;
import java.util.Objects;

public final class Mirror {

    private final URL mirroring;
    private final URL original;

    public Mirror(URL mirroring, URL original) {
        this.mirroring = mirroring;
        this.original = original;
    }

    public URL getMirroring() {
        return mirroring;
    }

    public URL getOriginal() {
        return original;
    }

    @Override
    public String toString() {
        return "Mirror{" +
                "mirror=" + mirroring +
                ", original=" + original +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Mirror mirror1 = (Mirror) o;
        return Objects.equals(mirroring, mirror1.mirroring) && Objects.equals(original, mirror1.original);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mirroring, original);
    }

}
