plugins {
  id("io.github.rognan.deno") version("0.1.0")
}

deno {
  version.set("%s")
}

tasks.register<io.github.rognan.deno.task.DenoExecTask>("denoExec") {
  args.set(listOf("--version"))
}
