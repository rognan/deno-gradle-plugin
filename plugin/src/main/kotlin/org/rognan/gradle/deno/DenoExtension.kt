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

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class DenoExtension internal constructor(project: Project) {
  val version: Property<String> = project.objects.property(String::class.java)
    .convention(DEFAULT_VERSION)

  companion object {
    private const val NAME = "deno"
    const val DEFAULT_VERSION = "1.14.0"

    @JvmStatic
    fun create(project: Project): DenoExtension {
      return project.extensions.create(NAME, DenoExtension::class.java, project)
    }
  }
}
