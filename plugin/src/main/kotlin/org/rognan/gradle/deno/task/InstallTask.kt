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
