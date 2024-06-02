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

package io.github.rognan.deno;

import io.github.rognan.deno.task.ExecTask;
import io.github.rognan.deno.task.InstallTask;
import io.github.rognan.deno.util.PlatformHelper;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.file.Directory;
import org.gradle.api.file.RegularFile;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.net.URI;

/**
 * Enable the use of <a href="https://deno.land/">Deno</a> in your Gradle build.
 * Example:
 * <pre>
 *   plugins {
 *     id("io.github.rognan.deno") version "0.1.0"
 *   }
 *
 *   deno {
 *     version.set("1.19.1")
 *   }
 *
 *   tasks.register&#60;io.github.rognan.deno.task.ExecTask&#62;("helloWorld") {
 *     args.set(listOf("eval", "console.log('Hello, World!');"))
 *   }
 * </pre>
 */
public class DenoPlugin implements Plugin<Project> {

  private final ObjectFactory objectFactory;

  @Inject
  public DenoPlugin(ObjectFactory objectFactory) {
    this.objectFactory = objectFactory;
  }

  @Override
  public void apply(@Nonnull Project project) {
    PlatformHelper helper = new PlatformHelper();
    DenoExtension extension = DenoExtension.create(project);
    ConfigurationContainer configurations = project.getConfigurations();

    Configuration configuration = configurations.create("deno", it -> {
      it.setDescription("Configuration for 'io.github.rognan.deno'");
      it.setCanBeConsumed(false);
      it.setCanBeResolved(true);
      it.setTransitive(false);
      it.setVisible(false);
    });

    project.afterEvaluate(it -> {
      if (configuration.getDependencies().isEmpty()) {
        project.getRepositories().ivy(repository -> {
          repository.setName("io.github.rognan.deno:denoland@github");
          repository.setUrl(URI.create("https://github.com/"));
          repository.patternLayout(layout -> layout.artifact(
            "[organization]/[module]/releases/download/v[revision]/[module]-[classifier].[ext]"
          ));
          repository.metadataSources(IvyArtifactRepository.MetadataSources::artifact);
          repository.content(descriptor -> {
            descriptor.includeGroup("denoland"); // only handle 'denoland' dependencies
            descriptor.onlyForConfigurations(configuration.getName());
          });
        });

        Dependency defaultDependency = project.getDependencies().create(
          extension.getVersion().map(helper::getDependencyNotation).get()
        );

        configuration.getDependencies().add(defaultDependency);
      }
    });

    Provider<RegularFile> archiveProvider = configuration
      .getElements()
      .map(it -> it.stream()
        .findFirst()
        .map(file -> objectFactory.fileProperty().fileValue(file.getAsFile()))
        .orElseThrow(() -> new IllegalStateException("Could not find deno dependency."))
        .get()
      );

    Directory baseInstallDir = project.getRootProject()
      .getLayout()
      .getProjectDirectory()
      .dir(".gradle")
      .dir("deno");

    Provider<Directory> installDirProvider = project.provider(() -> {
      String version = configuration.getDependencies()
        .stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Could not find deno dependency."))
        .getVersion();

      return baseInstallDir.dir(helper.getInstallDirName(version));
    });

    Provider<RegularFile> denoProvider = installDirProvider.flatMap(it ->
      project.provider(() -> it.file(helper.getExecutableName()))
    );

    TaskProvider<InstallTask> installTaskProvider = project.getTasks()
      .register(InstallTask.NAME, InstallTask.class, it -> {
        it.getArchive().set(archiveProvider);
        it.getDestinationDir().set(installDirProvider);
      });

    project.getTasks().withType(ExecTask.class).configureEach(it -> {
      it.dependsOn(installTaskProvider);
      it.getDeno().set(denoProvider);
    });
  }
}
