# Gradle Plugin for Deno

This plugin enables you to use [Deno](https://deno.land/) as part of your Gradle build.

## Documentation

### Requirements

- Gradle >= v6.0
- Java >= 11 (The one running Gradle)

### Install & Configure

```kotlin
plugins {
  id("org.rognan.gradle.deno-plugin")
}

deno {
  version.set("1.14.0")
}
```

## License

This project is Open Source software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
