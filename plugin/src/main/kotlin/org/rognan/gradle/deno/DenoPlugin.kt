package org.rognan.gradle.deno

import org.gradle.api.Plugin
import org.gradle.api.Project

class DenoPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    // Register a task
    project.tasks.register("greeting") { task ->
      task.doLast {
        println("Hello from plugin 'org.rognan.gradle.deno-plugin'")
      }
    }
  }
}
