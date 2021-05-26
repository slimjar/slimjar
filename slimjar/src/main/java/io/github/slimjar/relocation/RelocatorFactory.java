package io.github.slimjar.relocation;

import java.util.Collection;

public interface RelocatorFactory {
    Relocator create(final Collection<RelocationRule> rules);
}
