/*
 * Copyright © 2021-2024 the original author or authors.
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

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.rognan.deno.DenoExtension.DEFAULT_VERSION;
import static org.assertj.core.api.Assertions.assertThat;

class DenoExtensionTest {
  DenoExtension extension;

  @BeforeEach
  void setUp() {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("io.github.rognan.deno");
    extension = project.getExtensions().getByType(DenoExtension.class);
  }

  @Test
  void it_has_a_default_deno_version_by_convention() {
    assertThat(extension.getVersion().get()).isEqualTo(DEFAULT_VERSION);
  }
}
