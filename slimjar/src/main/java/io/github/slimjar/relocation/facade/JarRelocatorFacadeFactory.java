package io.github.slimjar.relocation.facade;

import io.github.slimjar.relocation.RelocationRule;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public interface JarRelocatorFacadeFactory {
    JarRelocatorFacade createFacade(final File input, final File output, final Collection<RelocationRule> relocationRules) throws IllegalAccessException, InstantiationException, InvocationTargetException;
}
