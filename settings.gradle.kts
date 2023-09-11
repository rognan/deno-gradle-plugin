dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("com.gradle.enterprise") version "3.14.1"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"

    val isCiServer = !env("CI").isNullOrEmpty()
    val isAct = env("ACT").toBoolean()
    publishAlwaysIf(isCiServer && !isAct)

    when {
      isAct -> tag("ACT")
      isCiServer && !isAct -> tag("CI")
      else -> tag("local")
    }

    if (isCiServer) {
      termsOfServiceAgree = "yes"
      link("VCS", linkToGitHubTagOrBranch())
      tag(env("GITHUB_REF_TYPE")) // branch or tag
      value("Commit", env("GITHUB_SHA"))
      value("Workflow", env("GITHUB_WORKFLOW"))
      value("Job", env("GITHUB_JOB"))
    }

    buildScanPublished {
      val shortcut = file("${rootDir}/build/reports/gradle-enterprise/build-scan.html")
      shortcut.parentFile.mkdirs()
      shortcut.writeText("""
        <!DOCTYPE HTML>
        <html>
         <head>
          <title>Build Scan</title>
          <meta http-equiv="refresh" content="0; url='$buildScanUri'">
         </head>
         <body>Redirecting you to $buildScanUri.</body>
        </html>
        """.trimIndent())
    }

    obfuscation {
      username { "[hidden]" }
      hostname { "[hidden]" }
      ipAddresses { listOf("[hidden]") }
    }
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
