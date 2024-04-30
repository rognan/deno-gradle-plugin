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
import org.gradle.api.file.ArchiveOperations;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.WorkResult;
import org.gradle.work.DisableCachingByDefault;

import javax.inject.Inject;

/**
 * Unpack given {@code archive} into {@code destinationDir}.
 */
@DisableCachingByDefault(because = "the dependency is already cached and decompressing the archive is about as expensive as restoring from cache")
public class InstallTask extends DefaultTask {

  /**
   * The default task group for {@link InstallTask}.
   */
  public static final String DEFAULT_GROUP = "Build Setup";

  /**
   * The task name
   */
  public static final String NAME = "denoInstall";
  private final ArchiveOperations archiveOperations;
  private final FileSystemOperations fileSystemOperations;

  /**
   * The zip archive to unpack into {@code destinationDir}.
   */
  @PathSensitive(PathSensitivity.ABSOLUTE)
  @InputFile
  private final RegularFileProperty archive;

  /**
   * The directory to unpack {@code archive} into.
   */
  @OutputDirectory
  private final DirectoryProperty destinationDir;

  @Inject
  public InstallTask(ObjectFactory objectFactory, ArchiveOperations archiveOperations, FileSystemOperations fileSystemOperations) {
    setGroup(DEFAULT_GROUP);
    setDescription("Install a project local version of deno.");

    this.archiveOperations = archiveOperations;
    this.fileSystemOperations = fileSystemOperations;

    this.archive = objectFactory.fileProperty();
    this.destinationDir = objectFactory.directoryProperty();
  }

  @TaskAction
  public WorkResult install() {
    return fileSystemOperations.copy(it -> {
      it.from(archiveOperations.zipTree(archive.getAsFile()));
      it.into(destinationDir);
    });
  }

  /**
   * @return the zip archive containing the deno executable
   */
  public RegularFileProperty getArchive() {
    return archive;
  }

  /**
   * @return the destination directory for the deno executable
   */
  public DirectoryProperty getDestinationDir() {
    return destinationDir;
  }
}
