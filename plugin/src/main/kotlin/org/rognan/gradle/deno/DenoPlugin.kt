/*
 * Copyright © 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rognan.gradle.deno

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.rognan.gradle.deno.task.DenoExecTask
import org.rognan.gradle.deno.task.InstallTask
import org.rognan.gradle.deno.util.DependencyHelper
import org.rognan.gradle.deno.util.PlatformInformation
import java.io.File
import java.net.URI

class DenoPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val helper = DependencyHelper()
    val platform = PlatformInformation()

    val extension = DenoExtension.create(project)

    val denoConfiguration: Configuration = project.configurations.create("deno") { configuration ->
      configuration.description = "Configuration for 'org.rognan.gradle.deno-plugin'"
      configuration.isCanBeConsumed = false
      configuration.isCanBeResolved = true
      configuration.isTransitive = false
      configuration.isVisible = false

      configuration.defaultDependencies { dependencySet ->
        dependencySet.add(
          project.dependencies.create(
            "${helper.organization()}:${helper.module()}:${extension.version.get()}" +
              ":${helper.classifier()}@${helper.extension()}"
          )
        )
      }
    }

    project.repositories.ivy { repository ->
      repository.name = "org.rognan.gradle.deno:denoland@github"
      repository.url = URI.create("https://github.com/")

      repository.patternLayout { layout ->
        layout.artifact(
          "[organization]/[module]/releases/download/v[revision]/[module]-[classifier].[ext]"
        )
      }

      repository.metadataSources { metadataSources ->
        metadataSources.artifact()
      }

      repository.content { descriptor ->
        // Only handle 'denoland:*:*' dependencies
        descriptor.includeGroup("denoland")

        // Only handle 'deno' configuration
        descriptor.onlyForConfigurations(denoConfiguration.name)
      }
    }

    val denoArchiveProvider: Provider<File> = denoConfiguration.elements.map { it.first().asFile }
    val installDirProvider: Provider<Directory> = project.provider {
      project.rootProject.layout.projectDirectory
        .dir(".gradle")
        .dir("deno")
        .dir("v${extension.version.get()}-${helper.classifier()}")
    }
    val denoProvider: Provider<RegularFile> = installDirProvider.flatMap {
      project.provider {
        it.file(
          when {
            platform.isWindows() -> "deno.exe"
            else -> "deno"
          }
        )
      }
    }

    val installTask = project.tasks.register(InstallTask.NAME, InstallTask::class.java) {
      it.archive.set(project.layout.file(denoArchiveProvider))
      it.destinationDir.set(installDirProvider)
    }

    project.tasks.withType(DenoExecTask::class.java).configureEach {
      it.dependsOn(installTask)
      it.deno.set(denoProvider)
    }
  }
}
