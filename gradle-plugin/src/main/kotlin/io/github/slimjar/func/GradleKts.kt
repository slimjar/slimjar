package io.github.slimjar.func

import io.github.slimjar.SLIM_API_CONFIGURATION_NAME
import io.github.slimjar.SLIM_CONFIGURATION_NAME
import org.gradle.api.Action
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.internal.Cast.uncheckedCast

/**
 * Adds `slim` configuration for Kotlin dsl with options
 */
fun DependencyHandler.slim(
    dependencyNotation: String,
    dependencyOptions: Action<ExternalModuleDependency>
): ExternalModuleDependency? {
    return withOptions(SLIM_CONFIGURATION_NAME, dependencyNotation, dependencyOptions)
}

/**
 * Adds `slimApi` configuration for Kotlin dsl with options
 */
fun DependencyHandler.slimApi(
    dependencyNotation: String,
    dependencyOptions: Action<ExternalModuleDependency>
): ExternalModuleDependency? {
    return withOptions(SLIM_API_CONFIGURATION_NAME, dependencyNotation, dependencyOptions)
}

/**
 * Alternative for adding `slim` configuration for Kotlin dsl but without options
 */
fun DependencyHandler.slim(dependencyNotation: Any): Dependency? = add(SLIM_CONFIGURATION_NAME, dependencyNotation)

/**
 * Alternative for adding `slimApi` configuration for Kotlin dsl but without options
 */
fun DependencyHandler.slimApi(dependencyNotation: Any): Dependency? =
    add(SLIM_API_CONFIGURATION_NAME, dependencyNotation)

/**
 * Creates a configuration with options
 */
private fun DependencyHandler.withOptions(
    configuration: String,
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency? = run {
    uncheckedCast<ExternalModuleDependency>(create(dependencyNotation)).also { dependency ->
        if (dependency == null) return@run null
        dependencyConfiguration.execute(dependency)
        add(configuration, dependency)
    }
}