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

import java.util.Properties;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlatformHelperTest {
  public static final String A_VERSION = "1.0.1";

  @ParameterizedTest
  @ArgumentsSource(DependencyNotationMatrixProvider.class)
  void it_provides_expected_dependency_notation(DependencyNotationMatrix matrix) {
    Properties properties = new Properties(System.getProperties());
    properties.setProperty("os.name", matrix.os);
    properties.setProperty("os.arch", matrix.arch);

    PlatformHelper helper = new PlatformHelper(properties);

    assertThat(helper.getDependencyNotation(A_VERSION))
      .isEqualTo(matrix.expected);
  }

  @ParameterizedTest
  @ArgumentsSource(ExecutableNameMatrixProvider.class)
  void it_provides_expected_executable_name(ExecutableNameMatrix matrix) {
    Properties properties = new Properties(System.getProperties());
    properties.setProperty("os.name", matrix.os);

    PlatformHelper helper = new PlatformHelper(properties);

    assertThat(helper.getExecutableName())
      .isEqualTo(matrix.expected);
  }

  @Test
  void it_throws_illegal_state_exception_when_unknown_os_name() {
    Properties properties = new Properties(System.getProperties());
    properties.setProperty("os.name", "TempleOS");

    PlatformHelper helper = new PlatformHelper(properties);

    assertThrows(IllegalStateException.class, () -> helper.getDependencyNotation("1.0.1"));
    assertThrows(IllegalStateException.class, helper::getExecutableName);
  }

  @Test
  void it_throws_illegal_state_exception_when_unknown_os_architecture() {
    Properties properties = new Properties(System.getProperties());
    properties.setProperty("os.arch", "foo");

    PlatformHelper helper = new PlatformHelper(properties);

    assertThrows(IllegalStateException.class, () -> helper.getDependencyNotation("1.0.1"));
  }

  record ExecutableNameMatrix(String os, String expected) {}
  record DependencyNotationMatrix(String os, String arch, String expected) {}
  static class DependencyNotationMatrixProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
        tuple("Mac OS X", "x86_64", "denoland:deno:1.0.1:x86_64-apple-darwin@zip"),
        tuple("Mac OS X", "amd64", "denoland:deno:1.0.1:x86_64-apple-darwin@zip"),
        tuple("Mac OS X", "aarch64", "denoland:deno:1.0.1:aarch64-apple-darwin@zip"),
        tuple("aix", "x86_64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("aix", "amd64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("nux", "x86_64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("nux", "amd64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("Linux", "x86_64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("Linux", "amd64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("FreeBSD", "x86_64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("FreeBSD", "amd64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("nix", "x86_64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("nix", "amd64", "denoland:deno:1.0.1:x86_64-unknown-linux-gnu@zip"),
        tuple("win", "x86_64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("win", "amd64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows 10", "x86_64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows 10", "amd64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows 8.1", "x86_64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows 8.1", "amd64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows 7", "x86_64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows 7", "amd64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows Vista", "x86_64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows Vista", "amd64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows XP", "x86_64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows XP", "amd64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows 2000", "x86_64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip"),
        tuple("Windows 2000", "amd64", "denoland:deno:1.0.1:x86_64-pc-windows-msvc@zip")
      ).map(Arguments::of);
    }

    @Nonnull
    private DependencyNotationMatrix tuple(String osName, String osArch, String expectedNotation) {
      return new DependencyNotationMatrix(osName, osArch, expectedNotation);
    }
  }

  static class ExecutableNameMatrixProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
        tuple("Mac OS X", "deno"),
        tuple("Mac OS X", "deno"),
        tuple("Mac OS X", "deno"),
        tuple("aix", "deno"),
        tuple("aix", "deno"),
        tuple("nux", "deno"),
        tuple("nux", "deno"),
        tuple("Linux", "deno"),
        tuple("Linux", "deno"),
        tuple("FreeBSD", "deno"),
        tuple("FreeBSD", "deno"),
        tuple("nix", "deno"),
        tuple("nix", "deno"),
        tuple("win", "deno.exe"),
        tuple("win", "deno.exe"),
        tuple("Windows 10", "deno.exe"),
        tuple("Windows 10", "deno.exe"),
        tuple("Windows 8.1", "deno.exe"),
        tuple("Windows 8.1", "deno.exe"),
        tuple("Windows 7", "deno.exe"),
        tuple("Windows 7", "deno.exe"),
        tuple("Windows Vista", "deno.exe"),
        tuple("Windows Vista", "deno.exe"),
        tuple("Windows XP", "deno.exe"),
        tuple("Windows XP", "deno.exe"),
        tuple("Windows 2000", "deno.exe"),
        tuple("Windows 2000", "deno.exe")
      ).map(Arguments::of);
    }

    @Nonnull
    private ExecutableNameMatrix tuple(String osName, String expected) {
      return new ExecutableNameMatrix(osName, expected);
    }
  }
}
