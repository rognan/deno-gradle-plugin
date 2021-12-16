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

package org.rognan.gradle.deno;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DenoPluginTest {
  private Project project;

  @BeforeEach
  void setUp() {
    project = ProjectBuilder.builder().build();
    project.getPlugins().apply("org.rognan.gradle.deno-plugin");
  }

  @Test
  void plugin_registers_task_with_name_denoInstall() {
    assertNotNull(project.getTasks().findByName("denoInstall"));
  }

  @Test
  void task_denoInstall_is_located_in_the_BuildSetup_task_group() {
    String group = project.getTasks()
      .findByName("denoInstall")
      .getGroup();

    assertThat(group).isEqualTo("Build Setup");
  }

  @Test
  void task_denoInstall_has_a_non_empty_default_description() {
    String description = project.getTasks()
      .findByName("denoInstall")
      .getDescription();

    assertThat(description).isNotBlank();
  }
}
