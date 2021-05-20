package io.github.slimjar.external;

import com.google.gson.Gson;
import io.github.slimjar.app.external.DependencyProvider;
import io.github.slimjar.reader.GsonDependencyDataProviderFactory;
import io.github.slimjar.relocation.JarFileRelocator;
import io.github.slimjar.relocation.RelocationRule;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;

import java.util.Collection;

public final class ExternalDependencyProvider implements DependencyProvider {
    private final Gson gson = new Gson();
    @Override
    public DependencyDataProviderFactory createDependencyDataProviderFactory() {
        return new GsonDependencyDataProviderFactory(gson);
    }

    @Override
    public Relocator createRelocator(final Collection<RelocationRule> relocationRules) {
        return new JarFileRelocator(relocationRules);
    }
}
