package io.github.slimjar.plugin.configuration

import io.github.slimjar.plugin.PLUGIN_ID
import io.github.slimjar.plugin.SLIM_CONFIG_NAME
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConfigurationTest {

    private val project = ProjectBuilder.builder().build()

    init {
        project.pluginManager.apply(PLUGIN_ID)
    }

    @Test
    fun `Test slim configuration exists`() {
        val config = project.configurations.findByName(SLIM_CONFIG_NAME)
        assertThat(config).isNotNull
    }

    /*@Test
    fun `Test slimApi configuration exists`() {
        val config = project.configurations.findByName(SIM_API_CONFIG_NAME)
        assertThat(config).isNotNull
    }*/

    @Test
    fun `Test add slim dependency`() {
        assertThatCode {
            project.dependencies.add(SLIM_CONFIG_NAME, "com.google.code.gson:gson:2.8.6")
        }.doesNotThrowAnyException()
    }

    /*@Test
    fun `Test add slimApi dependency`() {
        assertThatCode {
            project.dependencies.add(SIM_API_CONFIG_NAME, "com.google.code.gson:gson:2.8.6")
        }.doesNotThrowAnyException()
    }*/

}