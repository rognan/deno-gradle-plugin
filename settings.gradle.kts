dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("com.gradle.enterprise") version "3.7.2"
}

val isCiServer = !env("CI").isNullOrEmpty()

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"

    tag(if(isCiServer) "CI" else "Local")

    if (isCiServer) {
      termsOfServiceAgree = "yes"
      publishAlways()

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
      if (isCiServer) {
        username { "github" }
      } else {
        username { "[hidden]" }
        hostname { "[hidden]" }
        ipAddresses { listOf("[hidden]") }
      }
    }
  }
}

include("deno-plugin")

rootProject.name = "deno-gradle-plugin"

fun env(variable: String) = System.getenv(variable)
fun linkToGitHubTagOrBranch() = env("GITHUB_SERVER_URL") + // i.e. `https://github.com`
  "/${env("GITHUB_REPOSITORY")}" + // i.e. `rognan/deno-gradle-plugin`
  "/tree/${env("GITHUB_REF_NAME")}" // i.e. `tree/main`
