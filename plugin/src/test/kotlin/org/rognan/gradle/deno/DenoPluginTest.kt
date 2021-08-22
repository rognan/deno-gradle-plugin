package org.rognan.gradle.deno

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class DenoPluginTest {
  @Test
  fun `task 'denoInstall' is registered`() {
    val project = ProjectBuilder.builder().build()
    project.plugins.apply("org.rognan.gradle.deno-plugin")

    assertNotNull(project.tasks.findByName("denoInstall"))
  }
}
