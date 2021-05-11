<h1 align="center">Slim Jar</h1>
<h3 align="center">Runtime Dependency Management</h3>
<hr>

<h4 class="test">What is SlimJar?</h4>

SlimJar allows you to download and load up dependencies at runtime as an alternative to shading your dependencies. This helps you reduce build output size and share downloaded dependencies between projects at client side. It is built mainly with the gradle eco-system in mind and is easily configurable being an almost a drop-in replacement/add-on to gradle projects.

<h4 class="test">Why use SlimJar?</h4>

SlimJar makes the process of switching out jars easier by providing jars that are much lesser in size, all the "slimmed" dependencies are already available and do not need to be explicitly moved back to your working directory during and update or change. This can be extremely useful for users who have lower bandwidth connections to push large updates to production or testing environments. It also provides vital features such as package relocation, module isolation, auto configuration generation...etc with the simplicity of minor tweaks in your build file.

![](header.png)

## Usage example

Will be updated soon.

## Development setup

```sh
git clone https://github.com/Vshnv/slimjar.git
gradlew test
```

## Releases

* SlimJar is currently under initial developement.

Distributed under the MIT license. See ``LICENSE`` for more information.

## Contributing

1. Fork it (<https://github.com/vshnv/slimjar/fork>)
2. Create your feature branch (`git checkout -b feature/abcd`)
3. Commit your changes (`git commit -am 'Added some feature abcd'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request
