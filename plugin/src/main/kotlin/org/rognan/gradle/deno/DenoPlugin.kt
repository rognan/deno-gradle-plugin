package org.rognan.gradle.deno

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.rognan.gradle.deno.task.InstallTask

class DenoPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register(InstallTask.NAME, InstallTask::class.java)
  }
}
