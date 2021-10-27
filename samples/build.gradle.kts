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

var samples = projectDir.walk()
  .filter { it.isFile && it.name in listOf("gradlew", "gradlew.bat") }
  .map { it.parentFile }.toSet()
  .map { sampleDir: File ->
    tasks.register(sampleDir.toTaskName()) {
      group = "Sample Project"
      description = "Build sample project '${sampleDir.name}'."

      doLast {
        org.gradle.tooling.GradleConnector.newConnector()
          .forProjectDirectory(sampleDir)
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

tasks.named("build") {
  dependsOn(samples)
}

fun File.toTaskName() = name.split("-")
  .joinToString("", "build", "") {
    it.capitalize()
  }
