package io.github.slimjar.util;

import io.github.slimjar.resolver.data.Repository;

public final class Repositories {
    private Repositories() {

    }

    public static String fetchFormattedUrl(final Repository repository) {
        String repoUrl = repository.getUrl().toString();
        if (!repoUrl.endsWith("/")) {
            repoUrl += "/";
        }
        return repoUrl;
    }
}
