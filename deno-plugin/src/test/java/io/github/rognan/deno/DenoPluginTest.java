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

import io.github.rognan.deno.task.InstallTask;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static io.github.rognan.deno.DenoExtension.DEFAULT_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DenoPluginTest {
  @Nested
  @DisplayName("When 'apply plugin' and nothing else")
  class WhenDefaultsTest {
    ProjectInternal project;

    @BeforeEach
    void setUp() {
      project = (ProjectInternal) ProjectBuilder.builder().build();
      project.getPlugins().apply("io.github.rognan.deno");
      project.evaluate();
    }

    @Test
    void it_registers_deno_extension() {
      var extension = project.getExtensions()
        .getByType(DenoExtension.class);

      assertThat(extension).isNotNull();
    }

    @Test
    void it_adds_install_task() {
      var task = project.getTasks()
        .withType(InstallTask.class)
        .findByName("denoInstall");

      assertNotNull(task);
      assertThat(task.getGroup()).isEqualTo("Build Setup");
      assertThat(task.getDescription()).isEqualTo("Install a project local version of deno.");
    }

    @Test
    void it_adds_denoland_repository() {
      var repository = (IvyArtifactRepository) project.getRepositories()
        .findByName("io.github.rognan.deno:denoland@github");

      assertThat(repository).isNotNull();
      assertThat(repository.getUrl()).isEqualTo(URI.create("https://github.com/"));
      assertThat(repository.isAllowInsecureProtocol()).isFalse();
    }

    @Test
    void it_adds_dependency_in_deno_configuration() {
      var maybeDependency = project.getConfigurations().getByName("deno")
        .getDependencies()
        .stream().findFirst();

      assertThat(maybeDependency).isPresent();

      Dependency dependency = maybeDependency.get();
      assertThat(dependency.getVersion()).isEqualTo(DEFAULT_VERSION);
      assertThat(dependency.getGroup()).isEqualTo("denoland");
      assertThat(dependency.getName()).isEqualTo("deno");
    }
  }
}
