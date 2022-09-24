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

  plugins.create("denoPlugin") {
    id = "com.github.rognan.deno-plugin"
    displayName = "Deno Gradle Plugin"
    description = "Use Deno, a runtime for JavaScript and Typescript, as part of your Gradle build."
    implementationClass = "com.github.rognan.gradle.deno.DenoPlugin"
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
  archiveBaseName.set("deno-plugin")
}

tasks.named<Jar>("jar") {
  manifest.attributes["Implementation-Title"] = "deno-plugin"
  manifest.attributes["Implementation-Version"] = project.version
  manifest.attributes["Created-By"] =
    "${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})"

  from(rootDir) {
    include("LICENSE")
  }
}

dependencies {
  testImplementation("org.assertj:assertj-core:3.23.1")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
  "functionalTestRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

repositories {
  mavenCentral()
}
