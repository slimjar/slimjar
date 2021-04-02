# Slim Jar
> Runtime dependency management.

SlimJar allows you to download and load up dependencies at runtime. This reduces the initial Jar size, this is mostly useful when you have strict size requirements.

Alternative use cases would include reuse of downloaded dependencies when switching the application jar, sharing of dependency jars between multiple applications (if same version of the dependency were to be used).

![](header.png)

## Usage example

Will be updates soon.

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
