package io.github.vshnv.slimjar

import io.github.vshnv.slimjar.exceptions.ConfigurationNotFoundException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

const val CONFIGURATION = "slim"

class SlimJarPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        plugins.apply("java")

        val config = createSlimConfig()
        println(config)
    }

    private fun Project.createSlimConfig(): Configuration {
        val compileOnlyConfig = configurations.findByName("compileOnly")
            ?: throw ConfigurationNotFoundException("Could not find `compileOnly` configuration!")

        val slimConfig = configurations.create(CONFIGURATION)
        compileOnlyConfig.extendsFrom(slimConfig)
        
        return slimConfig
    }

}