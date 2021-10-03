plugins {
  `java-gradle-plugin`

  id("org.jetbrains.kotlin.jvm") version "1.5.31"
  id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}

repositories {
  mavenCentral()
}

val functionalTestSourceSet: SourceSet = sourceSets.create("functionalTest")

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
  group = "Verification"

  testClassesDirs = functionalTestSourceSet.output.classesDirs
  classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

tasks.check {
  dependsOn(functionalTest)
}

gradlePlugin {
  plugins.create("denoPlugin") {
    id = "org.rognan.gradle.deno-plugin"
    implementationClass = "org.rognan.gradle.deno.DenoPlugin"
  }
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
  testImplementation("org.assertj:assertj-core:3.21.0")
}
