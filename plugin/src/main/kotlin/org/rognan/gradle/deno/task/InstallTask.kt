package org.rognan.gradle.deno.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class InstallTask : DefaultTask() {
  init {
    group = DEFAULT_GROUP
    description = "Installs a local version of deno."
  }

  @TaskAction
  fun install() {
    println("Hello from plugin 'org.rognan.gradle.deno-plugin'")
  }

  companion object {
    const val NAME = "denoInstall"
    const val DEFAULT_GROUP = "Build Setup"
  }
}
