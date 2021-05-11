package io.github.slimjar.resolver.mirrors;

import io.github.slimjar.resolver.data.Mirror;
import io.github.slimjar.resolver.data.Repository;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public final class SimpleMirrorSelector implements MirrorSelector {
    private static final String DEFAULT_CENTRAL_MIRROR_URL = "https://repo.vshnv.tech/";
    private static final String CENTRAL_URL = "https://repo.maven.apache.org/maven2/";
    private static final String ALT_CENTRAL_URL = "https://repo1.maven.org/maven2/";
    private static final Collection<String> CENTRAL_REPO;

    static {
        final Set<String> central = new HashSet<>();
        central.add(CENTRAL_URL);
        central.add(ALT_CENTRAL_URL);
        CENTRAL_REPO = Collections.unmodifiableSet(central);
    }

    @Override
    public Collection<Repository> select(final Collection<Repository> mainRepositories, final Collection<Mirror> mirrors) throws MalformedURLException {
        final Collection<URL> originals = mirrors.stream()
                .map(Mirror::getOriginal)
                .collect(Collectors.toSet());
        final Collection<Repository> resolved = mainRepositories.stream()
                .filter(repo -> !originals.contains(repo.getUrl()))
                .filter(repo -> !isCentral(repo))
                .collect(Collectors.toSet());
        final Collection<Repository> mirrored = mirrors.stream()
                .map(Mirror::getMirroring)
                .map(Repository::new)
                .collect(Collectors.toSet());
        resolved.addAll(mirrored);
        resolved.add(new Repository(
                new URL(DEFAULT_CENTRAL_MIRROR_URL)
        ));
        return resolved;
    }

    private static boolean isCentral(final Repository repository) {
        final String url = repository.getUrl().toString();
        return CENTRAL_REPO.contains(url);
    }
}
