package io.github.vshnv.slimjar.plugin

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PluginTest {

    private val project = ProjectBuilder.builder().build()
    private val configName = "slim"

    init {
        project.pluginManager.apply("io.github.vshnv.slimjar")
    }

    @Test
    fun `Test apply java plugin`() {
        assertThatCode {
            project.plugins.getPlugin("java")
        }.doesNotThrowAnyException()
    }

    @Test
    fun `Test slim configuration exists`() {
        val config = project.configurations.findByName(configName)
        assertThat(config).isNotNull
    }

    @Test
    fun `Test add slim dependency`() {
        assertThatCode {
            project.dependencies.add(configName, "com.google.code.gson:gson:2.8.6")
        }.doesNotThrowAnyException()
    }

}