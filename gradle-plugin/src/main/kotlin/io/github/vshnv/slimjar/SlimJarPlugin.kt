package io.github.vshnv.slimjar

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.github.vshnv.slimjar.extension.SlimJarExtension
import io.github.vshnv.slimjar.func.createConfig
import io.github.vshnv.slimjar.task.SlimJar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.jvm.tasks.Jar

const val SLIM_CONFIGURATION_NAME = "slim"
const val SLIM_API_CONFIGURATION_NAME = "slimApi"
const val SLIM_JAR_TASK_NAME = "slimJar"

class SlimJarPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        // Applies Java if not present, since it's required for the compileOnly configuration
        plugins.apply(JavaPlugin::class.java)

        val slimConfig = createConfig(SLIM_CONFIGURATION_NAME, JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME)
        //val slimApiConfig = createConfig(SLIM_API_CONFIGURATION_NAME, JavaPlugin.COMPILE_ONLY_API_CONFIGURATION_NAME)

        val slimJar = tasks.register(SLIM_JAR_TASK_NAME, SlimJar::class.java)

        val shadow = tasks.findByName("shadowJar")

        val slimJarExtension = extensions.findByType(SlimJarExtension::class.java) ?: extensions.create(
            "slimJar",
            SlimJarExtension::class.java,
            this
        )

        if (shadow == null) {
            // TODO Create the task for relocating without shadow
            tasks.withType(Jar::class.java).first().finalizedBy(slimJar)
        } else {

            val shadowTask = tasks.withType(ShadowJar::class.java).firstOrNull() ?: return@with
            shadowTask.doFirst {
                slimJarExtension.relocations.forEach { shadowTask.relocate(it.first, it.second) }
            }

        }

        println(slimJar)

    }

}