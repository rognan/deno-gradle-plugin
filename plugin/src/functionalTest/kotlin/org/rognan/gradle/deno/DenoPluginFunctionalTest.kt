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

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import kotlin.test.BeforeTest

class DenoPluginFunctionalTest {
  @TempDir
  @JvmField
  val projectDir: File? = null

  private val denoVersion = "1.14.2"

  @BeforeTest
  fun `setUp`() {
    projectDir?.resolve("settings.gradle.kts")?.writeText(
      """
        buildCache {

        // Having the local cache in a temporary directory will ensure a clean build cache between
        // tests. The default local cache dir (A Gradle user home created by the Gradle Test Kit)
        // is re-used between tests.

        local {
            directory = file("${projectDir.resolve(".cache").toURI()}")
          }
        }
      """.trimIndent()
    )

    projectDir?.resolve("build.gradle.kts")?.writeText(
      """
          plugins {
            id("org.rognan.gradle.deno-plugin") version("0.1.0")
          }

          deno {
            version.set("$denoVersion")
          }

          tasks.register<org.rognan.gradle.deno.task.DenoExecTask>("denoExec") {
            args.set(listOf("--version"))
          }
      """.trimIndent()
    )
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "7.2",
      "6.9.1",
      "6.0.1"
    ]
  )
  fun `i can execute 'deno' after applying the plugin`(gradleVersion: String) {
    val buildResult = GradleRunner.create()
      .withGradleVersion(gradleVersion)
      .withPluginClasspath()
      .withProjectDir(projectDir)
      .withArguments(":denoExec")
      .forwardOutput()
      .build()

    val installTask: BuildTask? = buildResult.task(":denoInstall")
    val execTask: BuildTask? = buildResult.task(":denoExec")

    assertThat(installTask!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
    assertThat(execTask!!.outcome).isEqualTo(TaskOutcome.SUCCESS)

    assertThat(buildResult.output).contains("deno $denoVersion")
  }
}
