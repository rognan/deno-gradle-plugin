plugins {
  id("base")
  id("org.rognan.gradle.deno-plugin") version "0.1.0"
}

deno {
  version.set("1.16.3")
}

tasks.register<org.rognan.gradle.deno.task.DenoExecTask>("helloWorld") {
  args.set(listOf("eval", "console.log('Hello, World!');"))
}

tasks.named("build") {
  dependsOn("helloWorld")
}
