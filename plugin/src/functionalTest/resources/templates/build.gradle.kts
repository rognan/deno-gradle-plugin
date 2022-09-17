plugins {
  id("com.github.rognan.deno-plugin") version("0.1.0")
}

deno {
  version.set("%s")
}

tasks.register<com.github.rognan.gradle.deno.task.DenoExecTask>("denoExec") {
  args.set(listOf("--version"))
}
