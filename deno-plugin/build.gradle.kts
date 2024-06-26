buildscript {
  dependencyLocking {
    lockAllConfigurations()
    lockMode.set(LockMode.STRICT)
  }
}

plugins {
  id("java-gradle-plugin")
  id("build.conventions")
}

val jvmTestVersion: JavaLanguageVersion = JavaLanguageVersion.of(21)
val jvmReleaseVersion: JavaLanguageVersion = JavaLanguageVersion.of(8)

dependencyLocking {
  lockAllConfigurations()
  lockMode.set(LockMode.STRICT)
}

gradlePlugin {
  plugins.create("deno") {
    id = "io.github.rognan.deno"
    displayName = "Deno Gradle Plugin"
    description = "Use Deno, a runtime for JavaScript and Typescript, as part of your Gradle build."
    implementationClass = "io.github.rognan.deno.DenoPlugin"
  }
}

testing.suites {
  configureEach {
    if(this is JvmTestSuite) {
      useJUnitJupiter("5.10.2")
      dependencies {
        implementation("org.assertj:assertj-core:3.25.3")
      }
      this.targets.configureEach {
        testTask {
          javaLauncher.set(javaToolchains.launcherFor {
            languageVersion.set(jvmTestVersion)
          })
        }
      }
    }
  }

  val test by getting(JvmTestSuite::class)

  register<JvmTestSuite>("functionalTest") {
    // make plugin-under-test-metadata.properties accessible to TestKit
    gradlePlugin.testSourceSet(sources)
    targets.configureEach {
      testTask {
        shouldRunAfter(test)

        // required Java installations, in part, for backwards compatibility tests
        listOf(8, 11, 17, 21).forEach {
          val javaHome = javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(it))
          }.get().metadata.installationPath.asFile.absolutePath

          systemProperty("java${it}Home", javaHome)
        }
      }
    }
  }
}

java {
  toolchain {
    languageVersion.set(jvmReleaseVersion)
  }
  withJavadocJar()
  withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
  options.isIncremental = true
  options.compilerArgs = listOf("-Xlint:all", "-Werror")
  options.isDebug = !env("CI").isNullOrEmpty()
}

tasks.named<JavaCompile>("compileTestJava") {
  javaCompiler.set(javaToolchains.compilerFor {
    languageVersion.set(jvmTestVersion)
  })
}

tasks.named<JavaCompile>("compileFunctionalTestJava") {
  javaCompiler.set(javaToolchains.compilerFor {
    languageVersion.set(jvmTestVersion)
  })
}

tasks.check {
  dependsOn(testing.suites.named("functionalTest"))
}

tasks.withType<AbstractArchiveTask>().configureEach {
  isReproducibleFileOrder = true
  isPreserveFileTimestamps = false

  dirPermissions { unix("755") }
  filePermissions { unix("644") }
}

tasks.withType<AbstractArchiveTask>().configureEach {
  archiveBaseName.set("deno-gradle-plugin")
}

tasks.named<Jar>("jar") {
  manifest.attributes["Implementation-Title"] = "deno-gradle-plugin"
  manifest.attributes["Implementation-Version"] = project.version
  manifest.attributes["Created-By"] =
    "${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})"

  from(rootDir) {
    include("LICENSE")
  }
}

repositories {
  mavenCentral()
}

fun env(variable: String) = System.getenv(variable)

