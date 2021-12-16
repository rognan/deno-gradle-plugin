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

package org.rognan.gradle.deno.task;

import java.util.stream.Stream;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.process.ExecResult;

public class DenoExecTask extends DefaultTask {
  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  RegularFileProperty deno = getProject().getObjects().fileProperty();

  @Input
  ListProperty<String> args = getProject().getObjects().listProperty(String.class);

  @TaskAction
  public ExecResult run() {
    final String executable = deno.get().getAsFile().getAbsolutePath();

    return getProject().exec(it -> it.commandLine(Stream.concat(Stream.of(executable), args.get().stream()).toArray()));
  }

  public RegularFileProperty getDeno() {
    return deno;
  }

  public ListProperty<String> getArgs() {
    return args;
  }

  public void setArgs(ListProperty<String> args) {
    this.args = args;
  }
}
