package io.github.vshnv.slimjar.plugin.groovy

import io.github.vshnv.slimjar.plugin.APPLY_SHADOW
import io.github.vshnv.slimjar.plugin.APPLY_SLIMJAR
import io.github.vshnv.slimjar.plugin.SHADOW_BUILDSCRIPT
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
        settingsFile = testDir.newFile("settings.gradle")
        settingsFile.writeText(
            """
                rootProject.name = "slimjar-test"
            """.trimIndent()
        )
    }

    @Test
    fun `Test applies shadow`() {
        testDir.newFile("build.gradle").writeText(
            """
                $SHADOW_BUILDSCRIPT                
                $APPLY_SLIMJAR
                $APPLY_SHADOW
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(testDir.root)
            .withPluginClasspath()
            .withArguments("shadowJar")
            .build()

        assertThat(TaskOutcome.SUCCESS).isEqualTo(result.task(":shadowJar")?.outcome)
    }

}