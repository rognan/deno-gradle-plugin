package org.rognan.gradle.deno

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class DenoPluginFunctionalTest {
  @Test
  fun `task 'greeting' can be executed`() {
    // Setup
    val projectDir = File("build/functionalTest")
    projectDir.mkdirs()
    projectDir.resolve("settings.gradle").writeText("")
    projectDir.resolve("build.gradle").writeText(
      """
            plugins {
                id('org.rognan.gradle.deno-plugin')
            }
        """
    )

    // Run
    val runner = GradleRunner.create()
    runner.forwardOutput()
    runner.withPluginClasspath()
    runner.withArguments("greeting")
    runner.withProjectDir(projectDir)
    val result = runner.build()

    // Verify
    assertTrue(result.output.contains("Hello from plugin 'org.rognan.gradle.deno-plugin'"))
  }
}
