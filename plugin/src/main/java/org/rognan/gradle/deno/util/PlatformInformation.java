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

import java.util.Properties;

public class PlatformInformation {
  private final Properties properties;

  public PlatformInformation() {
    this(System.getProperties());
  }

  public PlatformInformation(Properties properties) {
    this.properties = properties;
  }

  public String arch() {
    String arch = properties.getProperty("os.arch");

    if (arch == null || arch.trim().equals("")) {
      arch = System.getProperty("os.arch");
    }

    arch = arch.toLowerCase();

    if (arch.contains("amd64") || arch.contains("x86_64")) {
      return "x86_64";
    }
    else if (arch.contains("aarch64")) {
      return "aarch64";
    }
    else {
      throw new IllegalStateException("Unsupported OS Architecture: " + arch);
    }
  }

  public String os() {
    String os = properties.getProperty("os.name");

    if (os == null || os.trim().equals("")) {
      os = System.getProperty("os.name");
    }

    os = os.toLowerCase();
    if (os.contains("mac")) {
      return "apple-darwin";
    } else if (os.contains("win")) {
      return "pc-windows-msvc";
    } else if (os.contains("nix") || os.contains("nux")|| os.contains("aix")|| os.contains("bsd")) {
      return "unknown-linux-gnu";
    } else {
      throw new IllegalStateException("Unsupported OS: " + os);
    }
  }

  public Boolean isWindows() {
    return os().equals("pc-windows-msvc");
  }
}
