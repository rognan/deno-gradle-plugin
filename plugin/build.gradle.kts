plugins {
  id("java-gradle-plugin")
  id("org.jetbrains.kotlin.jvm") version "1.5.31"
  id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

val functionalTestSourceSet: SourceSet = sourceSets.create("functionalTest")

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
  group = "Verification"

  testClassesDirs = functionalTestSourceSet.output.classesDirs
  classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
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
  dependsOn(functionalTest)
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

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
  testImplementation("org.assertj:assertj-core:3.21.0")
}
