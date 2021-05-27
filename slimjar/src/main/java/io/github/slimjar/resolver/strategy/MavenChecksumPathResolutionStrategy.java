package io.github.slimjar.resolver.strategy;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;

import java.util.Locale;

public final class MavenChecksumPathResolutionStrategy implements PathResolutionStrategy {
    private static final String PATH_FORMAT = "%s%s/%s/%s/%3$s-%4$s.jar.%5$s";
    private final String algorithm;

    public MavenChecksumPathResolutionStrategy(final String algorithm) {
        this.algorithm = algorithm.replaceAll("[ -]", "").toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String pathTo(Repository repository, Dependency dependency) {
        return String.format(
                PATH_FORMAT,
                repository.getUrl(),
                dependency.getGroupId().replace('.', '/'),
                dependency.getArtifactId(),
                dependency.getVersion(),
                algorithm
        );
    }
}
