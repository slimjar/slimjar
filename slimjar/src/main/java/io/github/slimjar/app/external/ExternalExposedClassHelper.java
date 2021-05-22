package io.github.slimjar.app.external;

import io.github.slimjar.app.Application;
import io.github.slimjar.relocation.RelocationRule;
import io.github.slimjar.relocation.Relocator;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.reader.*;

import java.util.*;

public class ExternalExposedClassHelper implements ExposedClassHelper {
    private static final Collection<Class<?>> EXPOSED_CLASSES = Arrays.asList(
            Application.class,
            RelocationRule.class,
            Relocator.class,
            DependencyProvider.class,
            DependencyDataProvider.class,
            DependencyDataProviderFactory.class,
            DependencyReader.class,
            FileDependencyDataProvider.class,
            DependencyData.class
    );
    private static final Map<String, Class<?>> RELOCATION_MAP = new HashMap<>();

    static {
        RELOCATION_MAP.put(fix("io#github#slimjar#app#Application"), Application.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#relocation#RelocationRule"), RelocationRule.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#relocation#Relocator"), Relocator.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#app#external#DependencyProvider"), DependencyProvider.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#resolver#reader#DependencyDataProvider"), DependencyDataProvider.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#resolver#reader#DependencyDataProviderFactory"), DependencyDataProviderFactory.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#resolver#reader#DependencyReader"), DependencyReader.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#resolver#reader#FileDependencyDataProvider"), FileDependencyDataProvider.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#resolver#reader#ModuleDependencyDataProvider"), ModuleDependencyDataProvider.class);
        RELOCATION_MAP.put(fix("io#github#slimjar#resolver#data#DependencyData"), DependencyData.class);

    }

    @Override
    public Collection<Class<?>> fetchExposedClasses() {
        return Collections.unmodifiableCollection(EXPOSED_CLASSES);
    }

    @Override
    public Map<String, Class<?>> fetchRemappedRelocations() {
        return Collections.unmodifiableMap(RELOCATION_MAP);
    }

    /**
     * This exists to bypass relocation so that original classes can be mapped back
     * @param input package name separated by #
     * @return proper package name
     */
    private static String fix(final String input) {
        return input.replace('#', '.');
    }
}
