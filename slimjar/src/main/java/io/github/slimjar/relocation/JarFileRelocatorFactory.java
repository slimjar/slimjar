package io.github.slimjar.relocation;

import io.github.slimjar.relocation.facade.JarRelocatorFacadeFactory;

import java.util.Collection;

public final class JarFileRelocatorFactory implements RelocatorFactory {
    private final JarRelocatorFacadeFactory relocatorFacadeFactory;

    public JarFileRelocatorFactory(JarRelocatorFacadeFactory relocatorFacadeFactory) {
        this.relocatorFacadeFactory = relocatorFacadeFactory;
    }

    @Override
    public Relocator create(Collection<RelocationRule> rules) {
        return new JarFileRelocator(rules, relocatorFacadeFactory);
    }
}
