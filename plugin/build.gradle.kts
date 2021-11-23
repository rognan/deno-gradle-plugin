buildscript {
  dependencyLocking {
    lockAllConfigurations()
    lockMode.set(LockMode.STRICT)
  }
}

plugins {
  id("java-gradle-plugin")
  id("org.jetbrains.kotlin.jvm") version "1.6.0"
  id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

val functionalTestSourceSet: SourceSet = sourceSets.create("functionalTest")
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

dependencyLocking {
  lockAllConfigurations()
  lockMode.set(LockMode.STRICT)
}

gradlePlugin {
  testSourceSets(functionalTestSourceSet)

  plugins.create("denoPlugin") {
    id = "org.rognan.gradle.deno-plugin"
    displayName = "Deno Gradle Plugin"
    description = "Use Deno, a runtime for JavaScript and Typescript, as part of your Gradle build."
    implementationClass = "org.rognan.gradle.deno.DenoPlugin"
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

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
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

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
  testImplementation("org.assertj:assertj-core:3.21.0")
}
