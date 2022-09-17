[![Build](https://github.com/rognan/deno-gradle-plugin/actions/workflows/main.yml/badge.svg)](https://github.com/rognan/deno-gradle-plugin/actions?query=branch%3Amain+event%3Apush)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue)](https://www.apache.org/licenses/LICENSE-2.0)

**NB! This plugin is not yet released.**

# Gradle Plugin for Deno

This plugin enables you to use [Deno][0] as part of your [Gradle][1] build.

Example:

```kotlin
plugins {
  id("com.github.rognan.deno-plugin") version "0.1.0"
}

deno {
  // deno is downloaded from https://github.com/denoland/deno/releases
  // and placed in ${rootProject}/.gradle/deno/v${denoVersion}-${arch}-${os}
  version.set("1.16.3")
}

// arguments are forwarded directly to deno
tasks.register<com.github.rognan.gradle.deno.task.DenoExecTask>("helloWorld") {
  args.set(listOf("eval", "console.log('Hello, World!');"))
}
```

[0]: https://deno.land/
[1]: https://gradle.org/
