plugins {
  /*
   * `idea-ext` extends Gradle's own `idea` plugin with settings for code style, facets, copyright,
   * run configurations, etc.
   */
  id("org.jetbrains.gradle.plugin.idea-ext")
}

// idea.project may be null during buildscript compilation
if (idea.project != null && project == rootProject) {
  idea {
    project {
      settings {
        doNotDetectFrameworks("android")

        copyright {
          useDefault = Copyright.profileName
          profiles {
            create(Copyright.profileName) {
              keyword = Copyright.keyword
              notice = Copyright.notice.trimIndent()
            }
          }
        }

        delegateActions {
          delegateBuildRunToGradle = true
        }

        encodings {
          encoding = "utf-8"
          bomPolicy = org.jetbrains.gradle.ext.EncodingConfiguration.BomPolicy.WITH_NO_BOM
          properties {
            encoding = "utf-8"
            transparentNativeToAsciiConversion = false
          }
        }
      }
    }
  }
}

val ideaInstructionsUri: java.net.URI = uri("https://github.com/rognan/deno-gradle-plugin/wiki/Working-with-the-Code#importing-into-intellij-idea")

if (idea.module != null) {
  tasks.named("ideaModule") {
    enabled = false
  }
}

if (idea.project != null) {
  setOf("ideaProject", "ideaWorkspace").forEach { taskName ->
    tasks.named(taskName) {
      enabled = false
    }
  }

  tasks.named("idea") {
    doFirst {
      throw RuntimeException("To import in IntelliJ IDEA, follow the instructions in $ideaInstructionsUri")
    }
  }
}

object Copyright {
  const val profileName = "apache-2.0"
  const val keyword = "Copyright"
  const val notice =
    """
    #set( ${'$'}inceptionYear = 2021 )
    Copyright © ${'$'}inceptionYear#if(${'$'}today.year!=${'$'}inceptionYear)-${'$'}today.year#end the original author or authors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    """
}

fun org.jetbrains.gradle.ext.ProjectSettings.codeStyle(configuration: org.jetbrains.gradle.ext.CodeStyleConfig.() -> Unit) = (this as ExtensionAware).configure(configuration)
fun org.jetbrains.gradle.ext.ProjectSettings.copyright(configuration: org.jetbrains.gradle.ext.CopyrightConfiguration.() -> Unit) = (this as ExtensionAware).configure(configuration)
fun org.jetbrains.gradle.ext.ProjectSettings.delegateActions(configuration: org.jetbrains.gradle.ext.ActionDelegationConfig.() -> Unit) = (this as ExtensionAware).configure(configuration)
fun org.jetbrains.gradle.ext.ProjectSettings.encodings(configuration: org.jetbrains.gradle.ext.EncodingConfiguration.() -> Unit) = (this as ExtensionAware).configure(configuration)
fun org.jetbrains.gradle.ext.ProjectSettings.runConfigurations(configuration: PolymorphicDomainObjectContainer<org.jetbrains.gradle.ext.RunConfiguration>.() -> Unit) = (this as ExtensionAware).configure<NamedDomainObjectContainer<org.jetbrains.gradle.ext.RunConfiguration>> {
  (this as PolymorphicDomainObjectContainer<org.jetbrains.gradle.ext.RunConfiguration>).apply(configuration)
}
fun org.gradle.plugins.ide.idea.model.IdeaProject.settings(configuration: org.jetbrains.gradle.ext.ProjectSettings.() -> Unit) = (this as ExtensionAware).configure(configuration)
