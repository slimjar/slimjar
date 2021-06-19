package io.github.slimjar.relocation.manifest;

import io.github.slimjar.relocation.RelocationRule;

import java.io.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public final class RelocatedManifestFixer implements ManifestFixer {
    private final HashProvider hashProvider;

    public RelocatedManifestFixer(final HashProvider hashProvider) {
        this.hashProvider = hashProvider;
    }

    @Override
    public void fix(Path jarPath, Collection<RelocationRule> rule) throws IOException, NoSuchAlgorithmException {
        try (FileSystem fs = FileSystems.newFileSystem(jarPath, null)) {
            final Path manifestPath = fs.getPath("/META-INF/MANIFEST.MF");
            if (!Files.exists(manifestPath)) {
                return;
            }

            final Map<String, String> pathRelocations = rule.stream()
                    .collect(Collectors.toMap(
                            relocation -> convertPackageToPath(relocation.getOriginalPackagePattern()),
                            relocation -> convertPackageToPath(relocation.getRelocatedPackagePattern())
                    ));
            final Manifest manifest;
            try (final InputStream mInputStream = Files.newInputStream(manifestPath)) {
                manifest = new Manifest(mInputStream);
                final Map<String, Attributes> manifestAttributes = manifest.getEntries();
                final Collection<String> keys = new ArrayList<>(manifestAttributes.keySet());
                for (final String key : keys) {
                    final String relocation = pathRelocations.keySet().stream().filter(key::startsWith).findFirst().orElse(null);
                    if (relocation == null) {
                        continue;
                    }
                    final String relocatedPath = key.replace(relocation, pathRelocations.get(relocation));
                    final Path relocatedClass = fs.getPath("/", relocatedPath);
                    if (!Files.exists(relocatedClass)) {
                        continue;
                    }
                    final Attributes attributes = manifestAttributes.get(key);
                    final Optional<Object> digestKey = attributes.keySet().stream()
                            .filter(attrKey -> attrKey.toString().endsWith("Digest")) // todo: Handle lingual digests
                            .findFirst();

                    if (digestKey.isPresent()) {
                        final String hash = hashProvider.hash(Files.newInputStream(relocatedClass), digestKey.get().toString().replace("-Digest", ""));
                        attributes.put(digestKey.get(), hash);
                    }
                    manifestAttributes.remove(key);
                    manifestAttributes.put(relocatedPath, attributes);
                }
            }
            try (final OutputStream mOutputStream = Files.newOutputStream(manifestPath, StandardOpenOption.TRUNCATE_EXISTING)) {
                manifest.write(mOutputStream);
            }
        }
    }

    private static String convertPackageToPath(final String pkg) {
        return pkg.replace('.','/');
    }
}
