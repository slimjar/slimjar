package io.github.vshnv.slimjar.plugin.groovy

import io.github.vshnv.slimjar.plugin.PLUGIN_ID
import org.assertj.core.api.Assertions.assertThat
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class GroovyBuildScriptTest {

    private val testDir = TemporaryFolder()
    private var settingsFile: File

    init {
        testDir.create()
        settingsFile = testDir.newFile("settings.gradle.kts")
        settingsFile.writeText("""
            rootProject.name = "hello-world"
        """.trimIndent())
    }

    @Test
    fun `Test contains slimJar task`() {
        testDir.newFile("build.gradle").writeText("""
            plugins {
                id '$PLUGIN_ID'
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testDir.root)
            .withPluginClasspath()
            .withArguments("build", "--stacktrace")
            .build()

        println(result.output)
        println(result.tasks)

        assertThat(TaskOutcome.SUCCESS).isEqualTo(result.task(":slimJar")?.outcome)
    }

}