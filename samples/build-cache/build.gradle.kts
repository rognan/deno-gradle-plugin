plugins {
  id("base")
  id("org.rognan.gradle.deno-plugin") version "0.1.0"
}

deno {
  version.set("1.15.3")
}

tasks.register<org.rognan.gradle.deno.task.DenoExecTask>("bundle") {
  group = "Deno"
  description = "Outputs single JavaScript file from sources"

  inputs.files(file("./import_map.json"), file("./src/index.ts"))
    .withPathSensitivity(PathSensitivity.RELATIVE)

  outputs.dir(file("$buildDir"))
  outputs.cacheIf { true }

  args.set(
    listOf(
      "bundle", "--import-map", "./import_map.json",
      "./src/index.ts", "./build/bundle.js"
    )
  )
}

tasks.named("build") {
  dependsOn("bundle")
}
