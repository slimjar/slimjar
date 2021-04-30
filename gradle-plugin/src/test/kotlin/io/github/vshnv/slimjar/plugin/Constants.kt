package io.github.vshnv.slimjar.plugin

const val PLUGIN_ID = "io.github.vshnv.slimjar"
const val SLIM_CONFIG_NAME = "slim"
const val SIM_API_CONFIG_NAME = "slimApi"

const val SHADOW_BUILDSCRIPT = """
    buildscript {
        repositories {
            jcenter()
        }
        dependencies {
            classpath 'com.github.jengelman.gradle.plugins:shadow:6.1.0'
        }
    }
"""

const val APPLY_SLIMJAR = """
    plugins {
        id '$PLUGIN_ID'
    }
"""

const val APPLY_SHADOW = "apply plugin: 'com.github.johnrengelman.shadow'"