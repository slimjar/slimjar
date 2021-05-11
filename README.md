<h1 align="center">Slim Jar</h1>
<h3 align="center">Runtime Dependency Management</h3>
<hr>

<h4>What is SlimJar?</h4>

SlimJar allows you to download and load up dependencies at runtime as an alternative to shading your dependencies. This helps you reduce build output size and share downloaded dependencies between projects at client side. It is built mainly with the gradle eco-system in mind and is easily configurable being an almost a drop-in replacement/add-on to gradle projects.

<h4>Why use SlimJar?</h4>

SlimJar makes the process of switching out jars easier by providing jars that are much lesser in size, all "slimmed" dependencies are already available and do not need to be explicitly moved back to your working directory during an update or change. This can be extremely useful for users who have lower bandwidth connections to push large updates to production or testing environments. It also provides vital features such as package relocation, module isolation, auto configuration generation...etc with the simplicity of minor tweaks in your build file.

<hr>

<h2 align="center">Usage Example</h2>
<br><br>

SlimJar provides 2 major ways to inject dependencies to your applications, Appending Applications and Isolated Module Applications: <br>
1. Isolated module applications use a bootstrap module to inject required dependencies to another module(s). <br>
2. Appending applications require you to provide a URLClassLoader to which slimjar would append the dependency URLs to. <br>

<br><br>

#### Isolated Module Application
Isolated module applications use a bootstrap module to inject required dependencies to another module(s). This requires you to have a separate module that serves as a bootstrap module, the recommended module name is simply `bootstrap`.
<br><br>
*Module: bootstrap*<br>
*ApplicationBootstrap.class*<br>
```java
public final class MyApplication {
  public static void main(String[] args) {
    final ApplicationConfiguration config = ApplicationConfiguration.createDefault("MyApplication");
    final ApplicationFactory appFactory = new ApplicationFactory(config);

    final Collection<String> modules = Collections.singleton("hello-world");

    final Application app = appFactory.createIsolatedApplication(modules, "example.project.ExampleApplication", args);
    app.start();
    // call app.stop() in a similar manner when needed to alert the application to stop (depends on how you handle the call).
  }
}
```
*build.gradle*
```groovy
plugins {
  id 'com.github.johnrengelman.shadow' version '6.0.0'
  id 'io.github.slimjar' version '[slimjar-gradle-version]'
}
slimJar {
  isolate project(':hello-world')
}
```

*Module: hello-world*<br>
*ExampleApplication.class*<br>
```java
package example.project;

public final class ExampleApplication extends Application {
  private final String[] args;
  private ExampleApplication(final String[] args) {
    this.args = args;
  }
  
  @Override
  public void start() {
    // Start up your application from here
  }
  
  @Override
  public void stop() {
    // Stop your application here
  }
}
```
*build.gradle*
```groovy
plugins {
  id 'com.github.johnrengelman.shadow' version '6.0.0'
}
dependencies {
  slim 'group.id:artifact.id:version'
}
```

<br><br>

#### Appending application
Appending applications require you to provide a URLClassLoader to which slimjar would append the dependency URLs to. This generally is NOT recommended and would not be useful above Java 8 as the application classloader provided is not a URLClassLoader, the exception to this would be if you are using a framework which does use a URLClassLoader to load your application. Even in that case, newer versions of Java enforce strong encapsulation to stop deep reflection and hence this would not work from Java 16 onwards.
<br><br>
*MyApplication.class*
```java
public final class MyApplication {
  public static void main(String[] args) {
    final ApplicationConfiguration config = ApplicationConfiguration.createDefault("MyApplication");
    final ApplicationFactory appFactory = new ApplicationFactory(config);
    final Application app = appFactory.createAppendingApplication((URLClassLoader)MyApplication.class.getClassLoader());
    // The depencies are now available in the classloader provided, in this case, the application classloader
    final SomeLibrary library = new SomeLibrary();
    library.test();
  }
}
```
(NOTE: If you have specified relocations and are running in a IDE or any environment that does not use the shadowjar-ed build file, use the `ignoreRelocation` flag while running by using `-DignoreRelocation` in your runner arguments)
*build.gradle*
```groovy
plugins {
  id 'com.github.johnrengelman.shadow' version '6.0.0'
  id 'io.github.slimjar' version '[slimjar-gradle-version]'
}
dependencies {
  slim 'group.id:artifact.id:version'
}

slimJar {
  relocate 'a.b.c' 'm.n.o'
}

```


## Development setup

```sh
git clone https://github.com/SlimJar/slimjar.git
gradlew test
```

## Releases

* https://plugins.gradle.org/plugin/io.github.slimjar
* https://repo.vshnv.tech/releases/io/github/slimjar/slimjar/1.0.0

Distributed under the MIT license. See ``LICENSE`` for more information.

## Contributing

1. Fork it (<https://github.com/SlimJar/slimjar/fork>)
2. Create your feature branch (`git checkout -b feature/abcd`)
3. Commit your changes (`git commit -am 'Added some feature abcd'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

[slimjar-gradle-version]: 1.1.0
