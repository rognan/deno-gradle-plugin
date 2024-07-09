dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

include("deno-plugin")

rootProject.name = "deno-gradle-plugin"

fun env(variable: String) = System.getenv(variable)
fun envOrDefault(key: String, defaultValue: String): String = System.getenv()
  .getOrDefault(key, defaultValue)
  .ifBlank { defaultValue }

fun linkToGitHubTagOrBranch() =
  envOrDefault("GITHUB_SERVER_URL", "https://github.com") +
  "/${envOrDefault("GITHUB_REPOSITORY", "rognan/deno-gradle-plugin")}" +
  "/tree/${envOrDefault("GITHUB_HEAD_REF", "main")}"
