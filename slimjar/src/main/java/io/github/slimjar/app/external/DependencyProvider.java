package io.github.slimjar.app.external;

import io.github.slimjar.relocation.RelocationRule;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;

import java.util.Collection;

public interface DependencyProvider {
    DependencyDataProviderFactory createDependencyDataProviderFactory();
    Relocator createRelocator(final Collection<RelocationRule> relocationRules);
}
