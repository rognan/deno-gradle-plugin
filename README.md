<!---freshmark shields
output = [
  link(image('build', 'https://github.com/{{org}}/{{name}}/actions/workflows/main.yml/badge.svg'), 'https://github.com/{{org}}/{{name}}/actions?query=branch%3A{{branch}}+event%3Apush'),
  link(shield('license', 'License', 'Apache 2.0', 'blue'), 'https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)'),
  link(shield('release', 'Latest', '{{version}}', 'blue'), 'CHANGELOG.md'),
].join('\n');
-->
[![build](https://github.com/rognan/deno-gradle-plugin/actions/workflows/main.yml/badge.svg)](https://github.com/rognan/deno-gradle-plugin/actions?query=branch%3Amain+event%3Apush)
[![license](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
[![release](https://img.shields.io/badge/Latest-unrealeased-blue.svg)](CHANGELOG.md)
<!---freshmark /shields -->

# Gradle Plugin for Deno

This plugin enables you to use [Deno][0] as part of your [Gradle][1] build:

```kotlin
plugins {
  id("io.github.rognan.deno") version "0.1.0"
}

deno {
  // deno is downloaded from https://github.com/denoland/deno/releases
  // and placed in ${rootProject}/.gradle/deno/v${denoVersion}-${arch}-${os}
  version.set("1.43.4")
}

// arguments are forwarded directly to deno
tasks.register<task.io.github.rognan.deno.ExecTask>("helloWorld") {
  args.set(listOf("eval", "console.log('Hello, World!');"))
}
```

[0]: https://deno.land/
[1]: https://gradle.org/
