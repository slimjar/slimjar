package io.github.vshnv.slimjar.task

import com.google.gson.GsonBuilder
import io.github.vshnv.slimjar.relocation.RelocationConfig
import io.github.vshnv.slimjar.relocation.RelocationRule
import io.github.vshnv.slimjar.resolver.Dependency
import io.github.vshnv.slimjar.resolver.DependencyData
import io.github.vshnv.slimjar.resolver.Repository
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableDependency
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableModuleResult
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

@CacheableTask
open class SlimJar @Inject constructor(private val config: Configuration) : DefaultTask() {

    val relocations = mutableSetOf<RelocationRule>()

    var mirror: String? = null

    init {
        group = "slimJar"
    }

    open fun relocate(original: String, relocated: String, configure: Action<RelocationConfig>? = null): SlimJar {
        val relocationConfig = RelocationConfig()
        configure?.execute(relocationConfig)
        val rule = RelocationRule(original, relocated, relocationConfig.exclusions, relocationConfig.inclusions)
        relocations.add(rule)
        return this
    }

    /**
     * Action to generate the json file inside the jar
     */
    @TaskAction
    internal fun createJson() = with(project) {

        val dependencies =
            RenderableModuleResult(config.incoming.resolutionResult.root).children.map { it.toSlimDependency() }

        val repositories = repositories.filterIsInstance<MavenArtifactRepository>()
            .filterNot { it.url.toString().startsWith("file") }
            .toSet()
            .map { Repository(it.url.toURL()) }

        if (dependencies.isEmpty() || repositories.isEmpty()) return

        val folder = File("${buildDir}/resources/main/")
        if (folder.exists().not()) folder.mkdirs()

        FileWriter(File(folder, "slimjar.json")).use {
            GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(DependencyData(repositories, dependencies, relocations), it)
        }
    }

}

/**
 * Turns a [RenderableDependency] into a [Dependency]] with all its transitives
 */
private fun RenderableDependency.toSlimDependency(): Dependency {
    val (group, artifact, version) = name.split(":")
    val transitive = mutableSetOf<Dependency>()
    collectTransitive(transitive, children)
    return Dependency(group, artifact, version, id.toString(), transitive)
}

/**
 * Recursively flattens the transitive dependencies
 */
private fun collectTransitive(transitive: MutableSet<Dependency>, dependencies: Set<RenderableDependency>) {
    for (dependency in dependencies) {
        val (group, artifact, version) = dependency.name.split(":")
        val dep = Dependency(group, artifact, version, dependency.id.toString(), emptySet())
        if (dep in transitive) continue
        transitive.add(dep)
        collectTransitive(transitive, dependency.children)
    }
}