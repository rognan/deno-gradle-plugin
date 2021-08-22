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
