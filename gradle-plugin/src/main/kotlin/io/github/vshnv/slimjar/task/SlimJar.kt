package io.github.vshnv.slimjar.task

import io.github.vshnv.slimjar.relocation.JarFileRelocator
import org.gradle.api.internal.file.copy.CopyAction
import org.gradle.api.tasks.CacheableTask
import org.gradle.jvm.tasks.Jar

@CacheableTask
open class SlimJar : Jar() {

    init {
        group = "slimJar"
    }

    open fun relocate(from: String, to: String): SlimJar {

        return this
    }

    override fun createCopyAction(): CopyAction {
        val file = archiveFile.orNull?.asFile ?: return super.createCopyAction()
        val relocator = JarFileRelocator()
        /*val rule = RelocationRule("me.mattstudios.annotations", "relocated.import")
        relocator.relocate(file, output, listOf(rule))
        println("relocating ${output.name}")*/
        return super.createCopyAction()
    }

}