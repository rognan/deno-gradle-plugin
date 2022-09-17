plugins {
  `java-gradle-plugin`

  id("org.jetbrains.kotlin.jvm") version "1.5.0"
  id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

gradlePlugin {
  plugins.create("denoPlugin") {
    id = "org.rognan.gradle.deno-plugin"
    implementationClass = "org.rognan.gradle.deno.DenoPlugin"
  }
}

val functionalTestSourceSet: SourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
  testClassesDirs = functionalTestSourceSet.output.classesDirs
  classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
  dependsOn(functionalTest)
}
