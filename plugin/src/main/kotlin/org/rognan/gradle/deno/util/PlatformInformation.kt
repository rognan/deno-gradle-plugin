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

import java.util.Properties

class PlatformInformation(private val properties: Properties = System.getProperties()) {
  fun arch(): String {
    val arch: String = properties.getProperty("os.arch") ?: System.getProperty("os.arch")

    return when {
      arch.contains("amd64", true) -> "x86_64"
      arch.contains("x86_64", true) -> "x86_64"
      arch.contains("aarch64", true) -> "aarch64"
      else -> error("Unsupported OS Architecture: $arch")
    }
  }

  fun os(): String {
    val name: String = properties.getProperty("os.name") ?: System.getProperty("os.name")

    return when {
      name.contains("mac", true) -> "apple-darwin"
      name.contains("win", true) -> "pc-windows-msvc"
      name.contains("nix", true) -> "unknown-linux-gnu"
      name.contains("nux", true) -> "unknown-linux-gnu"
      name.contains("aix", true) -> "unknown-linux-gnu"
      name.contains("bsd", true) -> "unknown-linux-gnu"
      else -> error("Unsupported OS: $name")
    }
  }

  fun isWindows(): Boolean {
    return when {
      os() == "pc-windows-msvc" -> true
      else -> false
    }
  }
}
