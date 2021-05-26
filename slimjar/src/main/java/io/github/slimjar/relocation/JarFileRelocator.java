package io.github.slimjar.relocation;

import io.github.slimjar.relocation.facade.JarRelocatorFacade;
import io.github.slimjar.relocation.facade.JarRelocatorFacadeFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public final class JarFileRelocator implements Relocator {
    private final Collection<RelocationRule> relocations;
    private final JarRelocatorFacadeFactory relocatorFacadeFactory;

    public JarFileRelocator(final Collection<RelocationRule> relocations, final JarRelocatorFacadeFactory relocatorFacadeFactory) {
        this.relocations = relocations;
        this.relocatorFacadeFactory = relocatorFacadeFactory;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void relocate(File input, File output) throws IOException, ReflectiveOperationException {
        output.getParentFile().mkdirs();
        output.createNewFile();
        final JarRelocatorFacade jarRelocator = relocatorFacadeFactory.createFacade(input,output, relocations);
        jarRelocator.run();
    }
}
