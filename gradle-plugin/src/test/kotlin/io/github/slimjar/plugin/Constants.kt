package io.github.slimjar.plugin

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import io.github.slimjar.SlimJarPlugin
import org.gradle.api.Project

const val PLUGIN_ID = "io.github.slimjar"
const val SLIM_CONFIG_NAME = "slim"
const val SIM_API_CONFIG_NAME = "slimApi"

const val SHADOW_BUILDSCRIPT = """
    buildscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath 'gradle.plugin.com.github.jengelman.gradle.plugins:shadow:7.0.0'
        }
    }
"""

const val APPLY_SLIMJAR = """
    plugins {
        id '$PLUGIN_ID'
    }
"""

const val APPLY_SHADOW = "apply plugin: 'com.github.johnrengelman.shadow'"

fun Project.applyPlugins() {
    project.pluginManager.apply(ShadowPlugin::class.java)
    project.pluginManager.apply(SlimJarPlugin::class.java)
}