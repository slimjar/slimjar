package io.github.slimjar.func

import io.github.slimjar.exceptions.ConfigurationNotFoundException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.kotlin.dsl.maven

/**
 * Checks in the gradle.properties if should or not add the slimJar dependency by default
 */
val Project.slimDefaultDependency: Boolean
    get() = findProperty("slimjar.default.dependency")?.toString()?.toBoolean() ?: true

/**
 * Adds the slimJar dependency to the project
 */
fun Project.applySlimLib(configuration: String = "implementation") {
    repositories.maven("https://repo.vshnv.tech/")
    dependencies.add(configuration, "io.github.slimjar:slimjar:1.0.0")
}

/**
 * Utility for creating a configuration that extends another
 */
fun Project.createConfig(configName: String, extends: String): Configuration {
    val compileOnlyConfig = configurations.findByName(extends)
        ?: throw ConfigurationNotFoundException("Could not find `$extends` configuration!")

    val slimConfig = configurations.create(configName)
    compileOnlyConfig.extendsFrom(slimConfig)
    slimConfig.isTransitive = true

    return slimConfig
}

