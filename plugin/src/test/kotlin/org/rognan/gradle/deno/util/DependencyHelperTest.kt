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

package org.rognan.gradle.deno.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DependencyHelperTest {
  private lateinit var provider: DependencyHelper

  @BeforeTest
  fun setUp() {
    provider = DependencyHelper()
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
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
    ]
  )
  fun `classifier ends with appropriate operating system identifier`(os: String) {
    val expected: Map<String, String> = mapOf(
      "Windows 2000" to "pc-windows-msvc",
      "Windows XP" to "pc-windows-msvc",
      "Windows Vista" to "pc-windows-msvc",
      "Windows 7" to "pc-windows-msvc",
      "Windows 8.1" to "pc-windows-msvc",
      "Windows 10" to "pc-windows-msvc",
      "win" to "pc-windows-msvc",
      "Mac OS X" to "apple-darwin",
      "FreeBSD" to "unknown-linux-gnu",
      "nix" to "unknown-linux-gnu",
      "Unix" to "unknown-linux-gnu",
      "nux" to "unknown-linux-gnu",
      "Linux" to "unknown-linux-gnu",
      "aix" to "unknown-linux-gnu"
    )

    val properties = java.util.Properties(System.getProperties())
    properties.setProperty("os.name", os)

    assertThat(DependencyHelper(PlatformInformation(properties)).classifier())
      .endsWith(expected[os])
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "x86_64",
      "amd64",
      "aarch64"
    ]
  )
  fun `classifier starts with appropriate architecture identifier`(arch: String) {
    val expected: Map<String, String> = mapOf(
      "x86_64" to "x86_64",
      "amd64" to "x86_64",
      "aarch64" to "aarch64"
    )

    val properties = java.util.Properties(System.getProperties())
    properties.setProperty("os.arch", arch)

    assertThat(DependencyHelper(PlatformInformation(properties)).classifier())
      .startsWith(expected[arch])
  }

  @Test
  fun `the architecture and operating system are separated by a dash in the classifier`() {
    val properties = java.util.Properties(System.getProperties())
    properties.setProperty("os.arch", "aarch64")
    properties.setProperty("os.name", "Linux")

    assertThat(DependencyHelper(PlatformInformation(properties)).classifier())
      .isEqualTo("aarch64-unknown-linux-gnu")
  }

  @Test
  fun `the organization is denoland`() {
    assertEquals("denoland", provider.organization())
  }

  @Test
  fun `the module is deno`() {
    assertEquals("deno", provider.module())
  }

  @Test
  fun `the extension is zip`() {
    assertEquals("zip", provider.extension())
  }
}
