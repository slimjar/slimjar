//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package io.github.slimjar.task

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.slimjar.SLIM_API_CONFIGURATION_NAME
import io.github.slimjar.SlimJarPlugin
import io.github.slimjar.func.performCompileTimeResolution
import io.github.slimjar.func.slimInjectToIsolated
import io.github.slimjar.relocation.RelocationConfig
import io.github.slimjar.relocation.RelocationRule
import io.github.slimjar.resolver.CachingDependencyResolver
import io.github.slimjar.resolver.ResolutionResult
import io.github.slimjar.resolver.data.Dependency
import io.github.slimjar.resolver.data.DependencyData
import io.github.slimjar.resolver.data.Mirror
import io.github.slimjar.resolver.data.Repository
import io.github.slimjar.resolver.enquirer.PingingRepositoryEnquirerFactory
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector
import io.github.slimjar.resolver.pinger.HttpURLPinger
import io.github.slimjar.resolver.pinger.URLPinger
import io.github.slimjar.resolver.strategy.MavenChecksumPathResolutionStrategy
import io.github.slimjar.resolver.strategy.MavenPathResolutionStrategy
import io.github.slimjar.resolver.strategy.MavenPomPathResolutionStrategy
import io.github.slimjar.resolver.strategy.MavenSnapshotPathResolutionStrategy
import io.github.slimjar.resolver.strategy.MediatingPathResolutionStrategy
import io.github.slimjar.resolver.strategy.PathResolutionStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableDependency
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableModuleResult
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type
import java.net.URL
import javax.inject.Inject

private val scope = CoroutineScope(IO)

@CacheableTask
abstract class SlimJar @Inject constructor(private val config: Configuration) : DefaultTask() {

    // Find by name since it won't always be present
    private val apiConfig = project.configurations.findByName(SLIM_API_CONFIGURATION_NAME)

    private val relocations = mutableSetOf<RelocationRule>()
    private val mirrors = mutableSetOf<Mirror>()
    private val isolatedProjects = mutableSetOf<Project>()

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val shadowWriteFolder = File("${project.buildDir}/resources/main/")

    @Input
    var shade = true

    val outputDirectory: File = File("${project.buildDir}/resources/slimjar/")
        @OutputDirectory
        get


    init {
        group = "slimJar"
        inputs.files(config)
    }

    open fun relocate(original: String, relocated: String): SlimJar {
        return addRelocation(original, relocated, null)
    }

    open fun relocate(original: String, relocated: String, configure: Action<RelocationConfig>): SlimJar {
        return addRelocation(original, relocated, configure)
    }

    open fun mirror(mirror: String, original: String) {
        mirrors.add(Mirror(URL(mirror), URL(original)))
    }

    open infix fun String.mirroring(original: String) {
        mirrors.add(Mirror(URL(this), URL(original)))
    }

    open fun isolate(proj: Project) {
        isolatedProjects.add(proj)

        if (proj.slimInjectToIsolated) {
            proj.pluginManager.apply(ShadowPlugin::class.java)
            proj.pluginManager.apply(SlimJarPlugin::class.java)
            proj.getTasksByName("slimJar", true).firstOrNull()?.let {
                it.setProperty("shade", false)
            }
        }

        val shadowTask = proj.getTasksByName("shadowJar", true).firstOrNull()
        val jarTask = shadowTask ?: proj.getTasksByName("jar", true).firstOrNull()
        jarTask?.let {
            dependsOn(it)
        }
    }

    /**
     * Action to generate the json file inside the jar
     */
    @TaskAction
    internal fun createJson() = with(project) {
        val dependencies =
            RenderableModuleResult(config.incoming.resolutionResult.root)
                .children
                .mapNotNull {
                    it.toSlimDependency()
                }.toMutableSet()
        // If api config is present map dependencies from it as well
        apiConfig?.let { config ->
            dependencies.addAll(
                RenderableModuleResult(config.incoming.resolutionResult.root)
                    .children
                    .mapNotNull {
                        it.toSlimDependency()
                    }
            )
        }

        val repositories = repositories.filterIsInstance<MavenArtifactRepository>()
            .filterNot { it.url.toString().startsWith("file") }
            .toSet()
            .map { Repository(it.url.toURL()) }

        // Note: Commented out to allow creation of empty dependency file
        // if (dependencies.isEmpty() || repositories.isEmpty()) return
        //println("Folder exists: ${folder.exists()}")
        if (outputDirectory.exists().not()) outputDirectory.mkdirs()

        val file = File(outputDirectory, "slimjar.json")

        FileWriter(file).use {
            gson.toJson(DependencyData(mirrors, repositories, dependencies, relocations), it)
        }

        // Copies to shadow's main folder
        if (shadowWriteFolder.exists().not()) shadowWriteFolder.mkdirs()
        file.copyTo(File(shadowWriteFolder, file.name), true)
    }

    // Finds jars to be isolated and adds them to final jar
    @TaskAction
    internal fun includeIsolatedJars() = with(project) {
        isolatedProjects.filter { it != this }.forEach {
            val shadowTask = it.getTasksByName("shadowJar", true).firstOrNull()
            val jarTask = shadowTask ?: it.getTasksByName("jar", true).firstOrNull()
            jarTask?.let { task ->
                val archive = task.outputs.files.singleFile
                if (outputDirectory.exists().not()) outputDirectory.mkdirs()
                val output = File(outputDirectory, "${it.name}.isolated-jar")
                archive.copyTo(output, true)

                // Copies to shadow's main folder
                if (shadowWriteFolder.exists().not()) shadowWriteFolder.mkdirs()
                output.copyTo(File(shadowWriteFolder, output.name), true)
            }
        }
    }

    @TaskAction
    internal fun generateResolvedDependenciesFile() = with(project) {
        if (project.performCompileTimeResolution.not()) return@with

        fun Collection<Dependency>.flatten(): MutableSet<Dependency> {
            return this.flatMap { it.transitive.flatten() + it }.toMutableSet()
        }

        val file = File(outputDirectory, "slimjar-resolutions.json")

        val mapType: Type = object : TypeToken<MutableMap<String, ResolutionResult>>() {}.type
        val preResolved: MutableMap<String, ResolutionResult> = if (file.exists()) {
            gson.fromJson(FileReader(file), mapType)
        } else {
            mutableMapOf()
        }
        val dependencies = RenderableModuleResult(config.incoming.resolutionResult.root)
            .children
            .mapNotNull {
                it.toSlimDependency()
            }.toMutableSet().flatten()

        val repositories = repositories.filterIsInstance<MavenArtifactRepository>()
            .filterNot { it.url.toString().startsWith("file") }
            .toSet()
            .map { Repository(it.url.toURL()) }

        val releaseStrategy: PathResolutionStrategy = MavenPathResolutionStrategy()
        val snapshotStrategy: PathResolutionStrategy = MavenSnapshotPathResolutionStrategy()
        val resolutionStrategy: PathResolutionStrategy =
            MediatingPathResolutionStrategy(releaseStrategy, snapshotStrategy)
        val pomURLCreationStrategy: PathResolutionStrategy = MavenPomPathResolutionStrategy()
        val checksumResolutionStrategy: PathResolutionStrategy =
            MavenChecksumPathResolutionStrategy("SHA-1", resolutionStrategy)
        val urlPinger: URLPinger = HttpURLPinger()
        val enquirerFactory = PingingRepositoryEnquirerFactory(
            resolutionStrategy,
            checksumResolutionStrategy,
            pomURLCreationStrategy,
            urlPinger
        )
        val mirrorSelector = SimpleMirrorSelector(setOf(Repository(URL("https://repo.maven.apache.org/maven2/"))))
        val resolver = CachingDependencyResolver(
            urlPinger,
            mirrorSelector.select(repositories, mirrors),
            enquirerFactory,
            mapOf()
        )
        val result: MutableMap<String, ResolutionResult> = runBlocking(IO) {
            dependencies
                // Filter to enforce incremental resolution
                .filter {
                    preResolved[it.toString()]?.let { pre ->
                        repositories.none { r ->
                            pre.repository.url.toString() == r.url.toString()
                        }
                    } ?: true
                }
                .map {
                    scope.async { it.toString() to resolver.resolve(it).orElse(null) }
                }
                .associate { it.await() }
                .filterValues { it != null }
                .toMutableMap()
        }

        preResolved.forEach {
            result.putIfAbsent(it.key, it.value)
        }

        if (outputDirectory.exists().not()) outputDirectory.mkdirs()

        FileWriter(file).use {
            gson.toJson(result, it)
        }

        // Copies to shadow's main folder
        if (shadowWriteFolder.exists().not()) shadowWriteFolder.mkdirs()
        file.copyTo(File(shadowWriteFolder, file.name), true)
    }


    /**
     * Internal getter required because Gradle will think an internal property is an action
     */
    internal fun relocations(): Set<RelocationRule> {
        return relocations
    }

    /**
     * Adds a relocation to the list, method had to be separated because Gradle doesn't support default values
     */
    private fun addRelocation(
        original: String,
        relocated: String,
        configure: Action<RelocationConfig>? = null
    ): SlimJar {
        val relocationConfig = RelocationConfig()
        configure?.execute(relocationConfig)
        val rule = RelocationRule(original, relocated, relocationConfig.exclusions, relocationConfig.inclusions)
        relocations.add(rule)
        return this
    }

    /**
     * Turns a [RenderableDependency] into a [Dependency]] with all its transitives
     */
    private fun RenderableDependency.toSlimDependency(): Dependency? {
        val transitive = mutableSetOf<Dependency>()
        collectTransitive(transitive, children)
        return id.toString().toDependency(transitive)
    }

    /**
     * Recursively flattens the transitive dependencies
     */
    private fun collectTransitive(transitive: MutableSet<Dependency>, dependencies: Set<RenderableDependency>) {
        for (dependency in dependencies) {
            val dep = dependency.id.toString().toDependency(emptySet()) ?: continue
            if (dep in transitive) continue
            transitive.add(dep)
            collectTransitive(transitive, dependency.children)
        }
    }

    /**
     * Creates a [Dependency] based on a string
     * group:artifact:version:snapshot - The snapshot is the only nullable value
     */
    private fun String.toDependency(transitive: Set<Dependency>): Dependency? {
        val values = split(":")
        val group = values.getOrNull(0) ?: return null
        val artifact = values.getOrNull(1) ?: return null
        val version = values.getOrNull(2) ?: return null
        val snapshot = values.getOrNull(3)

        return Dependency(group, artifact, version, snapshot, transitive)
    }
}