<h1 align="center">Slim Jar</h1>
<h3 align="center">Runtime Dependency Management</h3>
  <div align="center">
    <a href="https://github.com/SlimJar/slimjar/">
        <img src="https://img.shields.io/github/license/SlimJar/slimjar">
    </a>
    <a href="https://github.com/SlimJar/slimjar/actions/workflows/gradle.yml">
        <img src="https://github.com/SlimJar/slimjar/actions/workflows/gradle.yml/badge.svg">
    </a>
    <a href="https://plugins.gradle.org/plugin/io.github.slimjar">
        <img src="https://img.shields.io/maven-metadata/v.svg?label=gradle-plugin&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fio%2Fgithub%2Fslimjar%2Fio.github.slimjar.gradle.plugin%2Fmaven-metadata.xml">
    </a>
    <a href="https://repo.vshnv.tech/releases/io/github/slimjar/slimjar">
        <img src="https://img.shields.io/maven-metadata/v.svg?label=maven&metadataUrl=https%3A%2F%2Frepo.vshnv.tech%2Fio%2Fgithub%2Fslimjar%2Fslimjar%2Fmaven-metadata.xml">
    </a>
  </div>

<hr>

<h4>What is SlimJar?</h4>

SlimJar allows you to download and load up dependencies at runtime as an alternative to shading your dependencies. This helps you reduce build output size and share downloaded dependencies between projects at client side. It is built mainly with the gradle eco-system in mind and is easily configurable being an almost a drop-in replacement/add-on to gradle projects.

<h4>Why use SlimJar?</h4>

SlimJar makes the process of switching out jars easier by providing jars that are much lesser in size, all "slimmed" dependencies are already available and do not need to be explicitly moved back to your working directory during an update or change. This can be extremely useful for users who have lower bandwidth connections to push large updates to production or testing environments. It also provides vital features such as package relocation, module isolation, auto configuration generation...etc with the simplicity of minor tweaks in your build file.

<hr>

<h2 align="center">Usage Example</h2>
<h4 align="center">Note: Use the shadowJar task to compile your project</h4>
<br><br>


```java
// this needs to be ran before you reference your dependencies
ApplicationBuilder.appending("MyApplicationName").build()
```
(NOTE: If you have specified relocations and are running in a IDE or any environment that does not use the shadowjar-ed build file, use the `ignoreRelocation` flag while running by using `-DignoreRelocation` in your runner arguments)
*build.gradle* GROOVY DSL
```groovy
plugins {
  id 'com.github.johnrengelman.shadow' version '6.0.0'
  id 'io.github.slimjar' version '1.3.0'
}
dependencies {
  implementation slimjar("1.2.6")
  slim 'group.id:artifact.id:version'
}

slimJar {
  relocate 'a.b.c', 'm.n.o'
}
```

(For Kotlin DSL, to use the `slimjar` extension in dependencies block, you will need the following import - `import io.github.slimjar.func.slimjar`)

<br>
<br>
<h2 align="center">Development setup</h2>


```sh
git clone https://github.com/SlimJar/slimjar.git
gradlew test
```
<br>
<br>
<h2 align="center">Releases</h2>

* https://plugins.gradle.org/plugin/io.github.slimjar
* https://repo.vshnv.tech/releases/io/github/slimjar/slimjar/1.2.1

Distributed under the MIT license. See ``LICENSE`` for more information.

<br>
<br>
<h2 align="center">Contributing</h2>



1. Fork it (<https://github.com/SlimJar/slimjar/fork>)
2. Create your feature branch (`git checkout -b feature/abcd`)
3. Commit your changes (`git commit -am 'Added some feature abcd'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

