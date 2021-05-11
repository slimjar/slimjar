package io.github.slimjar.plugin.task

import io.github.slimjar.plugin.applyPlugins
import org.assertj.core.api.Assertions.assertThatCode
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskTest {

    private val project = ProjectBuilder.builder().build().also { it.applyPlugins() }

    @Test
    fun `Test slimJar task`() {
        assertThatCode {
            project.tasks.getByName("slimJar")
        }.doesNotThrowAnyException()
    }

}