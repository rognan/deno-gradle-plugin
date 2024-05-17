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

package io.github.rognan.deno.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.*;
import org.gradle.process.ExecOperations;
import org.gradle.process.ExecResult;

import javax.inject.Inject;
import java.util.stream.Stream;

/**
 * Execute deno with provided arguments.
 */
public class ExecTask extends DefaultTask {
  private final ExecOperations execOperations;

  @InputFile
  @PathSensitive(PathSensitivity.RELATIVE)
  private final RegularFileProperty deno;

  @Input
  private final ListProperty<String> args;

  @Inject
  public ExecTask(ObjectFactory objectFactory, ExecOperations execOperations) {
    this.execOperations = execOperations;
    this.deno = objectFactory.fileProperty();
    this.args = objectFactory.listProperty(String.class);
  }

  /**
   * @return the result of the execution
   */
  @TaskAction
  public ExecResult run() {
    final String executable = deno.get().getAsFile().getAbsolutePath();

    return execOperations.exec(it -> {
      it.commandLine(Stream.concat(Stream.of(executable), args.get().stream()).toArray());
    });
  }

  /**
   * @return the deno executable file
   */
  public RegularFileProperty getDeno() {
    return deno;
  }

  /**
   * @return arguments passed to deno on execution
   */
  public ListProperty<String> getArgs() {
    return args;
  }
}
