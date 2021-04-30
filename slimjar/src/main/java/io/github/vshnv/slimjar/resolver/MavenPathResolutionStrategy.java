package io.github.vshnv.slimjar.resolver;

import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.Repository;

public class MavenPathResolutionStrategy implements PathResolutionStrategy {
    private static final String PATH_FORMAT = "%s/%s/%s/%s/%2$s-%4$s.jar";
    @Override
    public String pathTo(Repository repository, Dependency dependency) {
        return String.format(
                PATH_FORMAT,
                repository.getUrl(),
                dependency.getArtifactId(),
                dependency.getGroupId(),
                dependency.getVersion()
        );
    }
}
