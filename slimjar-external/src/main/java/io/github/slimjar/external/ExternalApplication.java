package io.github.slimjar.external;

import io.github.slimjar.app.Application;
import io.github.slimjar.app.external.DependencyProvider;

public class ExternalApplication extends Application {
    private final DependencyProvider dependencyProvider = new ExternalDependencyProvider();

    @Override
    public boolean start() {
        registerFacade(DependencyProvider.class, dependencyProvider);
        return true;
    }
}
