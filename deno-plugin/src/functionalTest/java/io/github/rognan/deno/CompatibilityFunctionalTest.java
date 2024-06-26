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

import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;

class CompatibilityFunctionalTest {
  static final String propertiesFileTemplate = """
    org.gradle.java.home=%s
    """;

  static final String settingsFileTemplate = """
      buildCache {
        // local cache in temp dir ensures clean build cache between tests.
        // default local cache dir is re-used between tests.
        local {
          directory = File("%s")
        }
      }
    """;

  static final String buildFileTemplate = """
    plugins {
      id("io.github.rognan.deno") version("0.1.0")
    }

    tasks.register<io.github.rognan.deno.task.ExecTask>("denoExec") {
      args.set(listOf("--version"))
    }
    """;

  @TempDir
  File projectDir;

  @BeforeEach
  void setUp() throws IOException {
    var settingsFile = new File(projectDir, "settings.gradle.kts").toURI();
    var buildFile = new File(projectDir, "build.gradle.kts").toURI();
    var cacheDir = new File(projectDir, ".cache").toURI();

    write(settingsFile, format(settingsFileTemplate, cacheDir));
    write(buildFile, buildFileTemplate);
  }

  @Test
  void it_can_execute_in_projects_where_config_cache_is_enabled() throws IOException {
    var propertiesFile = new File(projectDir, "gradle.properties").toURI();
    write(propertiesFile, format(propertiesFileTemplate, windowsFriendlyPath(System.getProperty("java21Home"))));

    var buildResult = GradleRunner.create()
      .withGradleVersion("8.7")
      .withPluginClasspath()
      .withProjectDir(projectDir)
      .withArguments(":denoExec", "--configuration-cache")
      // Causes TestKit to run without a daemon that otherwise may complain about a mismatch between
      // the specified Java Home and the Java Home that the daemon resolved, which may occur when
      // symlinks are present as they are for some JDKs (Which occurred with Gradle 6.7 and JDK8).
      .withDebug(true)
      .build();

    var installTask = buildResult.task(":denoInstall");
    var execTask = buildResult.task(":denoExec");

    assertThat(installTask).isNotNull();
    assertThat(execTask).isNotNull();
    assertThat(installTask.getOutcome()).isEqualTo(SUCCESS);
    assertThat(execTask.getOutcome()).isEqualTo(SUCCESS);

    assertThat(buildResult.getOutput()).contains("deno 1.43.4");
  }

  @ParameterizedTest
  @MethodSource("compatibilityMatrix")
  void i_can_execute_the_plugin_with_specified_gradle_and_java(String gradleVersion, String javaHome) throws IOException {
    var propertiesFile = new File(projectDir, "gradle.properties").toURI();
    write(propertiesFile, format(propertiesFileTemplate, windowsFriendlyPath(javaHome)));

    var buildResult = GradleRunner.create()
      .withGradleVersion(gradleVersion)
      .withPluginClasspath()
      .withProjectDir(projectDir)
      .withArguments(":denoExec")
      // Causes TestKit to run without a daemon that otherwise may complain about a mismatch between
      // the specified Java Home and the Java Home that the daemon resolved, which may occur when
      // symlinks are present as they are for some JDKs (Which occurred with Gradle 6.7 and JDK8).
      .withDebug(true)
      .build();

    var installTask = buildResult.task(":denoInstall");
    var execTask = buildResult.task(":denoExec");

    assertThat(installTask).isNotNull();
    assertThat(execTask).isNotNull();
    assertThat(installTask.getOutcome()).isEqualTo(SUCCESS);
    assertThat(execTask.getOutcome()).isEqualTo(SUCCESS);

    assertThat(buildResult.getOutput()).contains("deno 1.43.4");
  }

  static void write(URI target, String content) throws IOException {
    Files.writeString(
      Path.of(target),
      content,
      UTF_8,
      CREATE, WRITE, DSYNC
    );
  }

  // support java LTS versions and non-EOL versions of Gradle.
  // see also https://docs.gradle.org/current/userguide/feature_lifecycle.html#eol_support
  static Stream<Arguments> compatibilityMatrix() {
    var java8Home = System.getProperty("java8Home");
    var java11Home = System.getProperty("java11Home");
    var java17Home = System.getProperty("java17Home");
    var java21Home = System.getProperty("java21Home");

    var gradle7 = Stream.of(java8Home, java11Home, java17Home)
      .map(it -> Arguments.of("7.6.4", it));

    var gradle8 = Stream.of(java8Home, java11Home, java17Home, java21Home)
      .map(it -> Arguments.of("8.8", it));

    return Stream.of(gradle7, gradle8).flatMap(it -> it);
  }

  String windowsFriendlyPath(String javaHome) {
    return javaHome.replace("\\", "\\\\");
  }
}
