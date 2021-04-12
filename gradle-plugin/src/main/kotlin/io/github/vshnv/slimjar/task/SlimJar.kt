package io.github.vshnv.slimjar.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask

@CacheableTask
open class SlimJar : DefaultTask() {

    open fun relocate(from: String, to: String): SlimJar {
        return this
    }

}