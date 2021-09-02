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

package io.github.slimjar

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.github.slimjar.exceptions.ShadowNotFoundException
import io.github.slimjar.func.applyReleaseRepo
import io.github.slimjar.func.applySnapshotRepo
import io.github.slimjar.func.createConfig
import io.github.slimjar.task.SlimJar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.maven

const val SLIM_CONFIGURATION_NAME = "slim"
const val SLIM_API_CONFIGURATION_NAME = "slimApi"
const val SLIM_JAR_TASK_NAME = "slimJar"
private const val RESOURCES_TASK = "processResources"
private const val SHADOW_ID = "com.github.johnrengelman.shadow"

class SlimJarPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        // Applies Java if not present, since it's required for the compileOnly configuration
        plugins.apply(JavaPlugin::class.java)

        if (!plugins.hasPlugin(SHADOW_ID)) {
            throw ShadowNotFoundException("SlimJar depends on the Shadow plugin, please apply the plugin. For more information visit: https://imperceptiblethoughts.com/shadow/")
        }

        val slimConfig = createConfig(
            SLIM_CONFIGURATION_NAME,
            JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME,
            JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME
        )
        if (plugins.hasPlugin("java-library")) {
            createConfig(
                SLIM_API_CONFIGURATION_NAME,
                JavaPlugin.COMPILE_ONLY_API_CONFIGURATION_NAME,
                JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME
            )
        }

        val slimJar = tasks.create(SLIM_JAR_TASK_NAME, SlimJar::class.java, slimConfig)
        // Auto adds the slimJar lib dependency
        afterEvaluate {
            if (applyReleaseRepo) {
                repositories.maven("https://repo.vshnv.tech/")
            }
            if (applySnapshotRepo) {
                repositories.maven("https://repo.vshnv.tech/snapshots/")
            }
        }
        project.dependencies.extra.set(
            "slimjar",
            asGroovyClosure("+") { version -> "io.github.slimjar:slimjar:$version" }
        )
        // Hooks into shadow to inject relocations
        val shadowTask = tasks.withType(ShadowJar::class.java).firstOrNull() ?: return
        shadowTask.doFirst {
            slimJar.relocations().forEach { rule ->
                shadowTask.relocate(rule.originalPackagePattern, rule.relocatedPackagePattern) {
                    rule.inclusions.forEach { include(it) }
                    rule.exclusions.forEach { exclude(it) }
                }
            }
        }

        /*slimJar.outputs.upToDateWhen {
            true
        }*/

        // Runs the task once resources are being processed to save the json file
        tasks.findByName(RESOURCES_TASK)?.finalizedBy(slimJar)
    }

}
