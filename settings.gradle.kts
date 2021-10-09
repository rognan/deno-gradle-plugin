dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

include(
  "plugin",
  "samples"
)

rootProject.name = "deno-gradle-plugin"
