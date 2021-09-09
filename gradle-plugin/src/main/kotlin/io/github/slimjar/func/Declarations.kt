//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

@file:Suppress("UNCHECKED_CAST")

package io.github.slimjar.func

import io.github.slimjar.exceptions.ConfigurationNotFoundException
import io.github.slimjar.slimJarLib
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * Checks in the gradle.properties if should or not resolve dependencies at compile time
 */
val Project.performCompileTimeResolution: Boolean
    get() = findProperty("slimjar.default.resolution.compile_time")?.toString()?.toBoolean() ?: true

/**
 * Checks in the gradle.properties if should or not add the slimJar repo by default
 */
val Project.applyReleaseRepo: Boolean
    get() = findProperty("slimjar.default.repo.releases.apply")?.toString()?.toBoolean() ?: true


/**
 * Checks in the gradle.properties if should or not add the slimJar snapshot repo by default
 */
val Project.applySnapshotRepo: Boolean
    get() = findProperty("slimjar.default.repo.snapshot.apply")?.toString()?.toBoolean() ?: false

/**
 * Checks in the gradle.properties if should or not add the slimJar plugin to isolated projects by default
 */
val Project.slimInjectToIsolated: Boolean
    get() = findProperty("slimjar.default.isolated.inject")?.toString()?.toBoolean() ?: true


/**
 * Utility for creating a configuration that extends another
 */
fun Project.createConfig(configName: String, vararg extends: String): Configuration {
    val compileOnlyConfig = extends.map {
        configurations.findByName(it) ?: throw ConfigurationNotFoundException("Could not find `$extends` configuration!")
    }

    val slimConfig = configurations.create(configName)
    compileOnlyConfig.forEach { it.extendsFrom(slimConfig)}
    slimConfig.isTransitive = true

    return slimConfig
}

/**
 * Extension for KDSL support
 */
fun DependencyHandlerScope.slimjar(version: String = "+"): String = slimJarLib(version)

/**
 * Extension for KDSL support
 */
fun DependencyHandler.slimjar(version: String = "+"): String = slimJarLib(version)
