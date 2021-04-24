package io.github.vshnv.slimjar.extension

open class SlimJarExtension {

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