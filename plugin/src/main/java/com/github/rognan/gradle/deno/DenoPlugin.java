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

package com.github.rognan.gradle.deno;

import com.github.rognan.gradle.deno.task.DenoExecTask;
import com.github.rognan.gradle.deno.task.InstallTask;
import com.github.rognan.gradle.deno.util.PlatformHelper;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.file.Directory;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URI;

/**
 * Enable the use of <a href="https://deno.land/">Deno</a> in your Gradle build.
 *
 * Example:
 * <pre>
 *   plugins {
 *     id("com.github.rognan.deno-plugin") version "0.1.0"
 *   }
 *
 *   deno {
 *     version.set("1.19.1")
 *   }
 *
 *   tasks.register&#60;com.github.rognan.gradle.deno.task.DenoExecTask&#62;("helloWorld") {
 *     args.set(listOf("eval", "console.log('Hello, World!');"))
 *   }
 * </pre>
 */
public class DenoPlugin implements Plugin<Project> {
  @Override
  public void apply(@Nonnull Project project) {
    PlatformHelper helper = new PlatformHelper();
    DenoExtension extension = DenoExtension.create(project);
    ConfigurationContainer configurations = project.getConfigurations();

    Configuration denoConfiguration = configurations.create("deno", (configuration) -> {
      configuration.setDescription("Configuration for 'com.github.rognan.gradle.deno-plugin'");
      configuration.setCanBeConsumed(false);
      configuration.setCanBeResolved(true);
      configuration.setTransitive(false);
      configuration.setVisible(false);

      configuration.defaultDependencies((dependencySet) -> dependencySet.add(
        project.getDependencies().create(
          extension.getVersion().map(helper::getDependencyNotation).get()
        ))
      );
    });

    final RepositoryHandler repositories = project.getRepositories();

    repositories.ivy((repository) -> {
      repository.setName("com.github.rognan.gradle.deno:denoland@github");
      repository.setUrl(URI.create("https://github.com/"));

      repository.patternLayout((layout) -> layout.artifact(
        "[organization]/[module]/releases/download/v[revision]/[module]-[classifier].[ext]"
      ));

      repository.metadataSources(IvyArtifactRepository.MetadataSources::artifact);

      repository.content((descriptor) -> {
        // Only handle 'denoland:*:*' dependencies
        descriptor.includeGroup("denoland");

        // Only handle 'deno' configuration
        descriptor.onlyForConfigurations(denoConfiguration.getName());
      });
    });

    Provider<File> denoArchiveProvider = denoConfiguration
      .getElements()
      .map((it) -> it.stream().findFirst().get().getAsFile());

    Provider<Directory> installDirProvider = project.provider(() -> {
      return extension.getVersion().map((theVersion) -> {
        return project.getRootProject()
          .getLayout()
          .getProjectDirectory()
          .dir(".gradle")
          .dir("deno")
          .dir(helper.getInstallDirName(theVersion));
      }).get();
    });

    Provider<RegularFile> denoProvider = installDirProvider.flatMap(it -> {
      return project.provider(() -> it.file(helper.getExecutableName()));
    });

    TaskProvider<InstallTask> installTaskProvider = project.getTasks()
      .register(InstallTask.NAME, InstallTask.class, (it) -> {
        it.getArchive().set(project.getLayout().file(denoArchiveProvider));
        it.getDestinationDir().set(installDirProvider);
      });

    project.getTasks().withType(DenoExecTask.class).configureEach((it) -> {
      it.dependsOn(installTaskProvider);
      it.getDeno().set(denoProvider);
    });
  }
}
