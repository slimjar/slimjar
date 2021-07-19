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