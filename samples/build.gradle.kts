buildscript {
  repositories {
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
  }

  dependencies {
    classpath("org.gradle:gradle-tooling-api:${gradle.gradleVersion}")
  }
}

plugins {
  id("base")
}

val scriptFiles = listOf("gradlew", "gradlew.bat")

tasks.named("build") {
  doLast {
    projectDir.walk()
      .filter { it.isFile && it.name in scriptFiles }
      .map { it.parentFile }.toSet()
      .forEach { sample ->
        org.gradle.tooling.GradleConnector.newConnector()
          .forProjectDirectory(sample)
          .connect()
          .use { connection ->
            connection
              .newBuild()
              .forTasks("build")
              .run()
          }
      }
  }
}
