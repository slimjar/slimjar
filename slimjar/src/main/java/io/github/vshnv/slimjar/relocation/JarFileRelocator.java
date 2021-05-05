package io.github.vshnv.slimjar.relocation;

import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public final class JarFileRelocator implements Relocator {
    private final Collection<RelocationRule> relocations;

    public JarFileRelocator(Collection<RelocationRule> relocations) {
        this.relocations = relocations;
    }

    @Override
    public void relocate(File input, File output) throws IOException {
        final Collection<Relocation> internalRelocation = relocations.stream().map(JarFileRelocator::parseRule).collect(Collectors.toList());
        output.getParentFile().mkdirs();
        output.createNewFile();
        final JarRelocator jarRelocator = new JarRelocator(input, output, internalRelocation);
        jarRelocator.run();
    }

    private static Relocation parseRule(final RelocationRule relocationRule) {
        return new Relocation(relocationRule.getOriginalPackagePattern(), relocationRule.getRelocatedPackagePattern(), relocationRule.getExclusions(), relocationRule.getInclusions());
    }
}
