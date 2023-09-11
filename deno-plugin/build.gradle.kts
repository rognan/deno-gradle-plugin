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

val jvmTestVersion: JavaLanguageVersion = JavaLanguageVersion.of(17)
val jvmReleaseVersion: JavaLanguageVersion = JavaLanguageVersion.of(8)
val functionalTestSourceSet: SourceSet = sourceSets.create("functionalTest")
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

dependencyLocking {
  lockAllConfigurations()
  lockMode.set(LockMode.STRICT)
}

gradlePlugin {
  testSourceSets(functionalTestSourceSet)

  plugins.create("deno") {
    id = "io.github.rognan.deno"
    displayName = "Deno Gradle Plugin"
    description = "Use Deno, a runtime for JavaScript and Typescript, as part of your Gradle build."
    implementationClass = "io.github.rognan.deno.DenoPlugin"
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
  javaCompiler.set(javaToolchains.compilerFor {
    languageVersion.set(jvmReleaseVersion)
  })

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

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  javaLauncher.set(javaToolchains.launcherFor {
    languageVersion.set(jvmTestVersion)
  })
}

val functionalTest by tasks.registering(Test::class) {
  group = "Verification"

  testClassesDirs = functionalTestSourceSet.output.classesDirs
  classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
  dependsOn(functionalTest)
}

tasks.withType<AbstractArchiveTask>().configureEach {
  isReproducibleFileOrder = true
  isPreserveFileTimestamps = false

  // 755 and 644, respectively, when converted from base-10 (decimal) to base-8 (octal)
  dirMode = 493
  fileMode = 420
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

dependencies {
  testImplementation("org.assertj:assertj-core:3.24.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
  "functionalTestRuntimeOnly"("org.junit.platform:junit-platform-launcher:1.10.0")
  "functionalTestRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

repositories {
  mavenCentral()
}

fun env(variable: String) = System.getenv(variable)
