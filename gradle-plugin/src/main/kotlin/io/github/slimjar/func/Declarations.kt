package io.github.slimjar.func

import io.github.slimjar.exceptions.ConfigurationNotFoundException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

/**
 * Checks in the gradle.properties if should or not add the slimJar dependency by default
 */
val Project.slimDefaultDependency: Boolean
    get() = findProperty("slimjar.default.dependency")?.toString()?.toBoolean() ?: true

/**
 * Utility for creating a configuration that extends another
 */
internal fun Project.createConfig(configName: String, extends: String): Configuration {
    val compileOnlyConfig = configurations.findByName(extends)
        ?: throw ConfigurationNotFoundException("Could not find `$extends` configuration!")

    val slimConfig = configurations.create(configName)
    compileOnlyConfig.extendsFrom(slimConfig)
    slimConfig.isTransitive = true

    return slimConfig
}