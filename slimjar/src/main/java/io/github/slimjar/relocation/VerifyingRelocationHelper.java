package io.github.slimjar.relocation;

import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.relocation.meta.MetaMediator;
import io.github.slimjar.resolver.data.Dependency;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class VerifyingRelocationHelper implements RelocationHelper {
    private final FilePathStrategy outputFilePathStrategy;
    private final Relocator relocator;
    private final String selfHash;
    private final MetaMediator metaMediator;

    public VerifyingRelocationHelper(final FilePathStrategy outputFilePathStrategy, final Relocator relocator, final MetaMediator metaMediator) throws URISyntaxException, NoSuchAlgorithmException, IOException {
        this.metaMediator = metaMediator;
        final URL jarURL = getClass().getProtectionDomain().getCodeSource().getLocation();

        this.outputFilePathStrategy = outputFilePathStrategy;
        this.relocator = relocator;
        this.selfHash = Charset.defaultCharset().decode(ByteBuffer.wrap(findHash(jarURL))).toString();
    }

    @Override
    public File relocate(Dependency dependency, File file) throws IOException {
        final File relocatedFile = outputFilePathStrategy.selectFileFor(dependency);
        if (relocatedFile.exists()) {
            final String ownerHash = metaMediator.readAttribute("slimjar.owner");
            if (selfHash.equals(ownerHash)) {
                return relocatedFile;
            }
        }
        relocator.relocate(file, relocatedFile);
        metaMediator.writeAttribute("slimjar.owner", selfHash);
        return relocatedFile;
    }

    private byte[] findHash(final URL url) throws NoSuchAlgorithmException, URISyntaxException, IOException {
        final Path jarPath = Paths.get(url.toURI());
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(Files.readAllBytes(jarPath));
    }
}
