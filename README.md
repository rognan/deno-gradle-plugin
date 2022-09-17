# Gradle Plugin for Deno

This plugin enables you to use [Deno](https://deno.land/) as part of your Gradle build.

**NB!** This plugin is not yet released on the Gradle Plugin Portal.

Example:
```kotlin
plugins {
  id("org.rognan.gradle.deno-plugin") version "0.1.0"
}

deno {
  version.set("1.14.2")
}

// execute deno with arguments
tasks.register<org.rognan.gradle.deno.task.DenoExecTask>("denoExec") {
  args.set(listOf("--help"))
}
```

## License

This project is Open Source software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
