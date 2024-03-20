import java.util.*

plugins {
  `kotlin-dsl`
}

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
  constraints {
    // Force newer version in transitive resolution
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.okio:okio:3.4.0")
    implementation("com.squareup.okio:okio-jvm:3.4.0")
    implementation("com.google.guava:guava:33.1.0-jre")
  }

  // The 1.0 version of idea-ext requires IntelliJ IDEA ≥ 2020.3
  implementation("gradle.plugin.org.jetbrains.gradle.plugin.idea-ext:gradle-idea-ext:1.1.7")
  implementation("com.diffplug.spotless-changelog:spotless-changelog-plugin-gradle:3.0.2")
  implementation("com.diffplug.spotless:spotless-plugin-gradle:6.23.2")
  runtimeOnly("org.openjdk.nashorn:nashorn-core:15.4")
}
