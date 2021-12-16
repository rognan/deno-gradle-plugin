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

package org.rognan.gradle.deno.util;

/**
 * Provides utility functions that helps identify which platform specific deno dependency to fetch
 * from the build assets available with each deno-release.
 */
public class DependencyHelper {
  private final PlatformInformation platform;

  public DependencyHelper() {
    this(new PlatformInformation());
  }

  public DependencyHelper(PlatformInformation platform) {
    this.platform = platform;
  }

  public String organization() {
    return "denoland";
  }

  public String module() {
    return "deno";
  }

  public String classifier() {
    return String.format("%s-%s", platform.arch(), platform.os());
  }

  public String extension() {
    return "zip";
  }
}
