package io.github.vshnv.slimjar.task

import org.gradle.api.tasks.CacheableTask
import org.gradle.jvm.tasks.Jar

@CacheableTask
open class SlimJar : Jar() {

    open fun relocate(from: String, to: String): SlimJar {
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
>>>>>>> Stashed changes
        return this
    }

}