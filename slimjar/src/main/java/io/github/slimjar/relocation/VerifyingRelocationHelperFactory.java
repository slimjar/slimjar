package io.github.slimjar.relocation;

import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.relocation.meta.MetaMediator;
import io.github.slimjar.relocation.meta.MetaMediatorFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public class VerifyingRelocationHelperFactory implements RelocationHelperFactory {
    private final FilePathStrategy relocationFilePathStrategy;
    private final MetaMediatorFactory mediatorFactory;

    public VerifyingRelocationHelperFactory(final FilePathStrategy relocationFilePathStrategy, final MetaMediatorFactory mediatorFactory) {
        this.relocationFilePathStrategy = relocationFilePathStrategy;
        this.mediatorFactory = mediatorFactory;
    }

    @Override
    public RelocationHelper create(final Relocator relocator) throws URISyntaxException, IOException, NoSuchAlgorithmException {
        return new VerifyingRelocationHelper(relocationFilePathStrategy, relocator, mediatorFactory);
    }
}
