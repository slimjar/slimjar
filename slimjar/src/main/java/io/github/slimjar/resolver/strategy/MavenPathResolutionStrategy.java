package io.github.slimjar.resolver.strategy;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;

public class MavenPathResolutionStrategy implements PathResolutionStrategy {
    private static final String PATH_FORMAT = "%s%s/%s/%s/%3$s-%4$s.jar";
    @Override
    public String pathTo(Repository repository, Dependency dependency) {
        return String.format(
                PATH_FORMAT,
                repository.getUrl(),
                dependency.getGroupId().replace('.', '/'),
                dependency.getArtifactId(),
                dependency.getVersion()
        );
    }
}
