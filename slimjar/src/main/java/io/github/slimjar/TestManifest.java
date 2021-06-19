package io.github.slimjar;

import io.github.slimjar.relocation.JarFileRelocator;
import io.github.slimjar.relocation.RelocationRule;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacade;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import io.github.slimjar.relocation.manifest.Base64HashProvider;
import io.github.slimjar.relocation.manifest.ManifestFixer;
import io.github.slimjar.relocation.manifest.RelocatedManifestFixer;

import java.io.*;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;

public class TestManifest {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, URISyntaxException, ReflectiveOperationException {
        final File file = new File("D:\\JavaIntellij\\Testing\\configurate-core-4.1.1.jar");
        final File out = new File("D:\\JavaIntellij\\Testing\\configurate-core-4.1.1-relocated.jar");
        final Collection<RelocationRule> relocationRules = Collections.singleton(new RelocationRule("org.spongepowered.configurate", "ha.ha.lol"));
        final Relocator rel = new JarFileRelocator(relocationRules, ReflectiveJarRelocatorFacadeFactory.create());
        rel.relocate(file, out);
        final ManifestFixer fixer = new RelocatedManifestFixer(new Base64HashProvider());
        fixer.fix(out.toPath(), relocationRules);
    }
}
