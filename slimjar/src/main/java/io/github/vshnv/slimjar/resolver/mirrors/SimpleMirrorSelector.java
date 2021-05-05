package io.github.vshnv.slimjar.resolver.mirrors;

import io.github.vshnv.slimjar.resolver.data.Mirror;
import io.github.vshnv.slimjar.resolver.data.Repository;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;

public final class SimpleMirrorSelector implements MirrorSelector {
    private static final String DEFUALT_CENTRAL_MIRROR_URL = "https://repo.vshnv.tech/";
    private static final String CENTRAL_URL = "https://repo.maven.apache.org/maven2/";
    private static final String ALT_CENTRAL_URL = "https://repo1.maven.org/maven2/";

    @Override
    public Collection<Repository> select(final Collection<Repository> mainRepositories, final Collection<Mirror> mirrors) throws MalformedURLException {
        final Collection<URL> originals = mirrors.stream()
                .map(Mirror::getOriginal)
                .collect(Collectors.toSet());
        final Collection<Repository> resolved = mainRepositories.stream()
                .filter(repo -> !originals.contains(repo.getUrl()))
                .collect(Collectors.toSet());
        final Collection<Repository> mirrored = mirrors.stream()
                .map(Mirror::getMirroring)
                .map(Repository::new)
                .collect(Collectors.toSet());
        boolean isCentralMirrored = resolved.stream()
                .map(Repository::getUrl)
                .map(URL::toString)
                .noneMatch(repo -> repo.equals(CENTRAL_URL) || repo.equals(ALT_CENTRAL_URL));
        resolved.addAll(mirrored);
        if (!isCentralMirrored) {
            resolved.add(new Repository(
                    new URL(DEFUALT_CENTRAL_MIRROR_URL)
            ));
        }
        return resolved;
    }
}
