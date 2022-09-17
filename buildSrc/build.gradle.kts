import java.util.Properties

plugins {
  `kotlin-dsl`
}

group = "org.rognan.gradle.deno-plugin"

val checkBuildConfiguration by tasks.registering {
  doLast {
    val buildSrcProperties = layout.projectDirectory.file("gradle.properties").asFile
    val rootProjectProperties = layout.projectDirectory.file("../gradle.properties").asFile

    if (rootProjectProperties.isFile || buildSrcProperties.isFile) {
      assertCompatible(buildSrcProperties, rootProjectProperties)
    }
  }
}

fun assertCompatible(
  buildSrcProperties: File,
  rootProjectProperties: File
) {

  val jvmArgs = listOf(
    readProperties(buildSrcProperties),
    readProperties(rootProjectProperties)
  ).map {
    it.getProperty("org.gradle.jvmargs")
  }.toSet()

  if (jvmArgs.size > 1) {
    throw GradleException(
      """
          ${rootProjectProperties.path} and ${buildSrcProperties.path} have different
          org.gradle.jvmargs, which may cause two daemons to be spawned on CI and in IDEA.

          Use the same org.gradle.jvmargs for both builds.
        """.trimIndent()
    )
  }
}

fun readProperties(propertiesFile: File) = Properties().apply {
  when {
    propertiesFile.isFile -> {
      propertiesFile.inputStream().use { fis -> load(fis) }
    }
    else -> {
      Properties()
    }
  }
}

tasks.withType<JavaCompile>().configureEach {
  options.release.set(8)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "1.8"
    allWarningsAsErrors = true
  }
}

tasks.check {
  dependsOn(checkBuildConfiguration)
}

dependencies {
  // The 1.0 version of idea-ext requires IntelliJ IDEA ≥ 2020.3
  implementation("gradle.plugin.org.jetbrains.gradle.plugin.idea-ext:gradle-idea-ext:1.0.1")
}
