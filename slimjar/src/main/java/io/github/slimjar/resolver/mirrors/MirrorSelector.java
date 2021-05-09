package io.github.slimjar.resolver.mirrors;

import io.github.slimjar.resolver.data.Mirror;
import io.github.slimjar.resolver.data.Repository;

import java.net.MalformedURLException;
import java.util.Collection;

public interface MirrorSelector {
    Collection<Repository> select(final Collection<Repository> mainRepositories, final Collection<Mirror> mirrors) throws MalformedURLException;
}
