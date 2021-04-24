package io.github.vshnv.slimjar.extension

import org.gradle.api.Project

open class SlimJarExtension(private val project: Project) {

    val relocations = mutableSetOf<Pair<String, String>>()

    /**
     *  Adds a relocation
     *  @param pattern The "from" location
     *  @param destination The "to" location
     */
    fun relocate(pattern: String, destination: String): SlimJarExtension {
        relocations.add(pattern to destination)
        return this
    }

}