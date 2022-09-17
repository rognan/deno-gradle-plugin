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
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult

open class DenoExecTask : DefaultTask() {
  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  val deno: RegularFileProperty = project.objects.fileProperty()

  @Input
  val args: ListProperty<String> = project.objects.listProperty(String::class.java)

  @TaskAction
  fun run(): ExecResult {
    val executable = deno.get().asFile.absolutePath
    val arguments = args.get().toTypedArray()

    return project.exec { it.commandLine(executable, *arguments) }
  }
}
