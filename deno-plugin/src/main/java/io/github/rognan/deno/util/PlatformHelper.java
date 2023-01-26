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

package io.github.rognan.deno.util;

import javax.annotation.Nonnull;
import java.util.Properties;

/**
 * Provides utility functions that help identify which platform specific deno dependency to fetch
 * from the build assets available with each deno-release.
 */
public class PlatformHelper {
  private final Properties properties;

  /**
   * Create new instance of {@link  PlatformHelper}
   */
  public PlatformHelper() {
    this(System.getProperties());
  }

  PlatformHelper(Properties properties) {
    this.properties = properties;
  }

  /**
   * @param version the version of deno to create a dependency notation of
   * @return the dependency notation string, for instance 'denoland:deno:1.0:aarch64-apple-darwin@zip'
   */
  @Nonnull
  public String getDependencyNotation(String version) {
    return String.format("denoland:deno:%s:%s@zip", version, classifier());
  }

  /**
   * @return 'deno.exe' if windows, else 'deno'
   */
  @Nonnull
  public String getExecutableName() {
    return isWindows() ? "deno.exe" : "deno";
  }

  /**
   * The platform specific installation directory where the deno dependency is unpacked during
   * installation.
   * @param version the deno version in use
   * @return the install directory name, for instance 'v1.0-aarch64-apple-darwin'
   */
  public String getInstallDirName(String version) {
    return String.format("v%s-%s", version, classifier());
  }

  private String classifier() {
    return String.format("%s-%s", arch(), os());
  }

  private String arch() {
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

  private String os() {
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

  private boolean isWindows() {
    return os().equals("pc-windows-msvc");
  }
}
