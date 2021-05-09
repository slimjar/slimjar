package io.github.vshnv.slimjar.app.module;


public class ModuleNotFoundException extends RuntimeException {
    public ModuleNotFoundException(String moduleName) {
        super("Could not find module in jar: " + moduleName);
    }
}
