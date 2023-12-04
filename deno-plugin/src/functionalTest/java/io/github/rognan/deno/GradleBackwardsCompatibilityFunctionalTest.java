/*
 * Copyright © 2021-2023 the original author or authors.
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

package io.github.rognan.deno;

import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

class GradleBackwardsCompatibilityFunctionalTest {
  private static final String denoVersion = "1.38.4";
  private static final String settingsFileTemplate = """
      buildCache {
        // Having the local cache in a temporary directory will ensure a clean build cache between
        // tests. The default local cache dir (A Gradle user home created by the Gradle Test Kit)
        // is re-used between tests.

        local {
          directory = file("%s")
        }
      }
    """;

  private static final String buildFileTemplate = """
    plugins {
      id("io.github.rognan.deno") version("0.1.0")
    }

    deno {
      version.set("%s")
    }

    tasks.register<io.github.rognan.deno.task.DenoExecTask>("denoExec") {
      args.set(listOf("--version"))
    }
    """;

  @TempDir
  File projectDir;

  @BeforeEach
  void setUp() throws IOException {
    URI settingsFile = new File(projectDir, "settings.gradle.kts").toURI();
    URI buildFile = new File(projectDir, "build.gradle.kts").toURI();
    URI cacheDir = new File(projectDir, ".cache").toURI();

    write(settingsFile, format(settingsFileTemplate, cacheDir));
    write(buildFile, format(buildFileTemplate, denoVersion));
  }

  @ParameterizedTest
  @ValueSource(strings = {"7.6.3", "8.5"})
  void i_can_execute_the_plugin_with_specified_gradle_version(String gradleVersion) {
    var buildResult = GradleRunner.create()
      .withGradleVersion(gradleVersion)
      .withPluginClasspath()
      .withProjectDir(projectDir)
      .withArguments(":denoExec")
      .forwardOutput()
      .build();

    BuildTask installTask = buildResult.task(":denoInstall");
    BuildTask execTask = buildResult.task(":denoExec");

    assertThat(installTask.getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    assertThat(execTask.getOutcome()).isEqualTo(TaskOutcome.SUCCESS);

    assertThat(buildResult.getOutput()).contains("deno " + denoVersion);
  }

  private static void write(URI target, String content) throws IOException {
    Files.writeString(
      Path.of(target),
      content,
      UTF_8,
      StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.DSYNC
    );
  }
}
