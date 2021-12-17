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

package com.github.rognan.gradle.deno.util;

import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DependencyHelperTest {

  private DependencyHelper provider;

  @BeforeEach
  void setUp() {
    provider = new DependencyHelper();
  }

  @ParameterizedTest
  @ValueSource(
    strings = {
      "Windows 2000",
      "Windows XP",
      "Windows Vista",
      "Windows 7",
      "Windows 8.1",
      "Windows 10",
      "win",
      "Mac OS X",
      "nix",
      "FreeBSD",
      "Linux",
      "nux",
      "aix"
    }
  )
  void classifier_ends_with_appropriate_operating_system_identifier(String os) {
    Map<String, String> expected = Map.ofEntries(
      Map.entry("Windows 2000", "pc-windows-msvc"),
      Map.entry("Windows XP", "pc-windows-msvc"),
      Map.entry("Windows Vista", "pc-windows-msvc"),
      Map.entry("Windows 7", "pc-windows-msvc"),
      Map.entry("Windows 8.1", "pc-windows-msvc"),
      Map.entry("Windows 10", "pc-windows-msvc"),
      Map.entry("win", "pc-windows-msvc"),
      Map.entry("Mac OS X", "apple-darwin"),
      Map.entry("FreeBSD", "unknown-linux-gnu"),
      Map.entry("nix", "unknown-linux-gnu"),
      Map.entry("Unix", "unknown-linux-gnu"),
      Map.entry("nux", "unknown-linux-gnu"),
      Map.entry("Linux", "unknown-linux-gnu"),
      Map.entry("aix", "unknown-linux-gnu")
    );

    Properties properties = new Properties(System.getProperties());
    properties.setProperty("os.name", os);

    assertThat(new DependencyHelper(new PlatformInformation(properties)).classifier())
      .endsWith(expected.get(os));
  }

  @ParameterizedTest
  @ValueSource(
    strings = {
      "x86_64",
      "amd64",
      "aarch64"
    }
  )
  void classifier_starts_with_appropriate_architecture_identifier(String arch) {
    Map<String, String> expected = Map.of(
      "x86_64", "x86_64",
      "amd64", "x86_64",
      "aarch64", "aarch64"
    );

    Properties properties = new java.util.Properties(System.getProperties());
    properties.setProperty("os.arch", arch);

    assertThat(new DependencyHelper(new PlatformInformation(properties)).classifier())
      .startsWith(expected.get(arch));
  }

  @Test
  void the_architecture_and_operating_system_are_separated_by_a_dash_in_the_classifier() {
    Properties properties = new java.util.Properties(System.getProperties());
    properties.setProperty("os.arch", "aarch64");
    properties.setProperty("os.name", "Linux");

    assertThat(new DependencyHelper(new PlatformInformation(properties)).classifier())
      .isEqualTo("aarch64-unknown-linux-gnu");
  }

  @Test
  void the_organization_is_denoland() {
    assertEquals("denoland", provider.organization());
  }

  @Test
  void the_module_is_deno() {
    assertEquals("deno", provider.module());
  }

  @Test
  void the_extension_is_zip() {
    assertEquals("zip", provider.extension());
  }
}
