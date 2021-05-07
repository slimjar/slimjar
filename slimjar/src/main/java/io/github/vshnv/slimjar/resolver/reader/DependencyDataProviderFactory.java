package io.github.vshnv.slimjar.resolver.reader;



import org.json.simple.parser.JSONParser;

import java.net.URL;

public final class DependencyDataProviderFactory {
    private final JSONParser jsonParser = new JSONParser();
    public DependencyDataProvider create(final URL dependencyFileURL) {
        final DependencyReader dependencyReader = new SimpleJsonDependencyReader(jsonParser);
        return new FileDependencyDataProvider(dependencyReader, dependencyFileURL);
    }
}
