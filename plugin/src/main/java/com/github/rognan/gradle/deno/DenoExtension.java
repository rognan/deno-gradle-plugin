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

package com.github.rognan.gradle.deno;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class DenoExtension {
  public static final String NAME = "deno";
  public static final String DEFAULT_VERSION = "1.16.3";

  private Property<String> version;

  public DenoExtension(Project project) {
    version = project.getObjects().property(String.class)
      .convention(DEFAULT_VERSION);
  }

  public Property<String> getVersion() {
    return version;
  }

  public void setVersion(Property<String> version) {
    this.version = version;
  }

  public static DenoExtension create(Project project) {
    return project.getExtensions().create(NAME, DenoExtension.class, project);
  }
}
