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

package org.rognan.gradle.deno.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.WorkResult

/**
 * Unpacks given {@code archive} into {@code destinationDir}.
 */
// Making this task cacheable probably serves no purpose, the dependency itself is already cached so
// there's no network overhead to speak of, and unzipping the file is about as expensive as
// restoring it from a cache.
open class InstallTask : DefaultTask() {

  /**
   * The zip archive to unpack into {@code destinationDir}.
   */
  @get:PathSensitive(PathSensitivity.ABSOLUTE)
  @get:InputFile
  val archive: RegularFileProperty = project.objects.fileProperty()

  /**
   * The directory to unpack {@code archive} into.
   */
  @get:OutputDirectory
  val destinationDir: DirectoryProperty = project.objects.directoryProperty()

  init {
    group = DEFAULT_GROUP
    description = "Installs a local version of deno."
  }

  @TaskAction
  fun install(): WorkResult {
    return project.copy {
      it.from(project.zipTree(archive.asFile))
      it.into(destinationDir)
    }
  }

  companion object {
    const val DEFAULT_GROUP = "Build Setup"
    const val NAME = "denoInstall"
  }
}
