package io.github.vshnv.slimjar

import io.github.vshnv.slimjar.func.createConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

const val SLIM_CONFIGURATION_NAME = "slim"
const val SLIM_API_CONFIGURATION_NAME = "slimApi"
const val SLIM_JAR_TASK_NAME = "slimJar"

class SlimJarPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        // Applies Java if not present, since it's required for the compileOnly configuration
        plugins.apply("java-library")

        val slimConfig = createConfig(SLIM_CONFIGURATION_NAME, JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME)
        val slimApiConfig = createConfig(SLIM_API_CONFIGURATION_NAME, JavaPlugin.COMPILE_ONLY_API_CONFIGURATION_NAME)


        val slimJar = tasks.create(SLIM_JAR_TASK_NAME)

        println(slimJar)

    }

}