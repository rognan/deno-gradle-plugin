[![Build](https://github.com/rognan/deno-gradle-plugin/actions/workflows/main.yml/badge.svg)](https://github.com/rognan/deno-gradle-plugin/actions?query=branch%3Amain+event%3Apush)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue)](https://www.apache.org/licenses/LICENSE-2.0)

**NB! This plugin is not yet released.**

# Gradle Plugin for Deno

This plugin enables you to use [Deno](https://deno.land/) as part of your Gradle build.

Example:

```kotlin
plugins {
  id("org.rognan.gradle.deno-plugin") version "0.1.0"
}

deno {
  // deno is downloaded from the releases at
  // https://github.com/denoland/deno/
  // and placed in .gradle/deno/..
  version.set("1.14.3")
}

// arguments are forwarded directly to deno as is
tasks.register<org.rognan.gradle.deno.task.DenoExecTask>("helloWorld") {
  args.set(listOf("eval", "console.log('Hello, World!');"))
}
```

## License

This project is Open Source software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
