package io.github.slimjar.plugin.plugin

import io.github.slimjar.plugin.PLUGIN_ID
import org.assertj.core.api.Assertions.assertThatCode
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PluginTest {

    private val project = ProjectBuilder.builder().build()

    init {
        project.pluginManager.apply(PLUGIN_ID)
    }

    @Test
    fun `Test apply java-library plugin`() {
        assertThatCode {
            project.plugins.getPlugin("java")
        }.doesNotThrowAnyException()
    }

}