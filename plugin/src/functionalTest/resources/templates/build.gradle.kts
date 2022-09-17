plugins {
  id("org.rognan.gradle.deno-plugin") version("0.1.0")
}

deno {
  version.set("%s")
}

tasks.register<org.rognan.gradle.deno.task.DenoExecTask>("denoExec") {
  args.set(listOf("--version"))
}
