package io.github.slimjar.func

import io.github.slimjar.exceptions.ConfigurationNotFoundException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

/**
 * Utility for creating a configuration that extends another
 */
internal fun Project.createConfig(configName: String, extends: String): Configuration {
    val compileOnlyConfig = configurations.findByName(extends)
        ?: throw ConfigurationNotFoundException("Could not find `$extends` configuration!")

    val slimConfig = configurations.create(configName)
    compileOnlyConfig.extendsFrom(slimConfig)
    // TODO need to test this one, probably doesn't do what I think it does
    slimConfig.isTransitive = true

    return slimConfig
}