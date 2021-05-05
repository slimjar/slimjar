package io.github.slimjar.relocation

/**
 * Added as a wrapper for the [RelocationRule] due to Gradle not liking it
 */
class RelocationConfig {

    internal val inclusions = mutableListOf<String>()
    internal val exclusions = mutableListOf<String>()

    fun include(vararg pattern: String): RelocationConfig {
        // Shadow does some normalization, might be worth looking into
        inclusions.addAll(pattern)
        return this
    }

    fun exclude(vararg pattern: String): RelocationConfig {
        // Shadow does some normalization, might be worth looking into
        exclusions.addAll(pattern)
        return this
    }

}