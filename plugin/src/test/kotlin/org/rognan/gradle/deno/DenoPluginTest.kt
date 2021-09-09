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
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class DenoPluginTest {
  private lateinit var project: Project

  @BeforeTest
  fun setUp() {
    project = ProjectBuilder.builder().build()
    project.plugins.apply("org.rognan.gradle.deno-plugin")
  }

  @Test
  fun `plugin registers task with name 'denoInstall'`() {
    assertNotNull(project.tasks.findByName("denoInstall"))
  }

  @Test
  fun `task 'denoInstall' is located in the 'Build Setup' task group`() {
    val group = project.tasks.findByName("denoInstall")
      ?.group

    assertEquals("Build Setup", group)
  }

  @Test
  fun `task 'denoInstall' has a non-empty default description`() {
    val description = project.tasks.findByName("denoInstall")
      ?.description?.replace("\\s".toRegex(), "")

    assertNotNull(description)
    assertNotEquals("", description)
  }
}
