package io.github.vshnv.slimjar.task

import io.github.vshnv.slimjar.relocation.JarFileRelocator
import io.github.vshnv.slimjar.relocation.RelocationRule
import org.gradle.api.internal.file.copy.CopyAction
import org.gradle.api.tasks.CacheableTask
import org.gradle.jvm.tasks.Jar
import java.io.File

@CacheableTask
open class SlimJar : Jar() {

    init {
        group = "slimJar"
        println("init")
    }

    open fun relocate(from: String, to: String): SlimJar {

        return this
    }

    override fun createCopyAction(): CopyAction {
        val file = archiveFile.orNull?.asFile ?: return super.createCopyAction()
        val relocator = JarFileRelocator()
        val output = File("C:/Users/xpsyk/Desktop", "test.jar")
        val rule = RelocationRule("me.mattstudios.annotations", "relocated.import")
        relocator.relocate(file, output, listOf(rule))
        println("relocating ${output.name}")
        return super.createCopyAction()
    }

}