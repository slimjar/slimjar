package io.github.slimjar.resolver.strategy;

import io.github.slimjar.resolver.data.Dependency;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.util.Repositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class MavenSnapshotPathResolutionStrategy implements PathResolutionStrategy {
    private static final String PATH_FORMAT_ALT = "%s%s/%s/%s-SNAPSHOT/%4$s-%s/%3$s-%4$s-%5$s.jar";
    private static final String PATH_FORMAT = "%s%s/%s/%s-SNAPSHOT/%3$s-%4$s-%5$s.jar";


    @Override
    public Collection<String> pathTo(final Repository repository, final Dependency dependency) {
        final String repoUrl = Repositories.fetchFormattedUrl(repository);
        final String version = dependency.getVersion().replace("-SNAPSHOT", "");
        final String alt = String.format(
                PATH_FORMAT_ALT,
                repoUrl,
                dependency.getGroupId().replace('.', '/'),
                dependency.getArtifactId(),
                version,
                dependency.getSnapshotId()
        );
        final String general = String.format(
                PATH_FORMAT,
                repoUrl,
                dependency.getGroupId().replace('.', '/'),
                dependency.getArtifactId(),
                version,
                dependency.getSnapshotId()
        );
        return Arrays.asList(general, alt);
    }

}
