package io.github.slimjar

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.github.slimjar.exceptions.ShadowNotFoundException
import io.github.slimjar.func.createConfig
import io.github.slimjar.func.slimDefaultDependency
import io.github.slimjar.task.SlimJar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.maven

const val SLIM_CONFIGURATION_NAME = "slim"
const val SLIM_API_CONFIGURATION_NAME = "slimApi"
const val SLIM_JAR_TASK_NAME = "slimJar"
private const val RESOURCES_TASK = "processResources"

class SlimJarPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        // Applies Java if not present, since it's required for the compileOnly configuration
        plugins.apply(JavaPlugin::class.java)

        if (!plugins.hasPlugin(ShadowPlugin::class.java)) {
            throw ShadowNotFoundException("SlimJar depends on the Shadow plugin, please apply the plugin. For more information visit: https://imperceptiblethoughts.com/shadow/")
        }

        val slimConfig = createConfig(SLIM_CONFIGURATION_NAME, JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME)
        if (plugins.hasPlugin("java-library")) {
            createConfig(SLIM_API_CONFIGURATION_NAME, JavaPlugin.COMPILE_ONLY_API_CONFIGURATION_NAME)
        }

        val slimJar = tasks.create(SLIM_JAR_TASK_NAME, SlimJar::class.java, slimConfig)

        // Auto adds the slimJar lib dependency
        afterEvaluate {
            if (slimDefaultDependency) {
                repositories.maven("https://repo.vshnv.tech/")
                dependencies.add("implementation", "io.github.slimjar:slimjar:1.0.0")
            }
        }

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

        // Runs the task once resources are being processed to save the json file
        tasks.findByName(RESOURCES_TASK)?.finalizedBy(slimJar)
    }

}