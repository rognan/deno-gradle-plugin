# Contributing Guide

Thank you for wanting to contribute! 🥳👌

**Table of Contents**

<details>
  <summary>Click to expand</summary>

  - [Request Feature](#request-feature)
  - [Report Vulnerability](#report-vulnerability)
  - [Report Bug(s)](#report-bugs)
  - [Make Changes](#make-changes)
    - [Pre-requisites](#pre-requisites)
    - [Configure Git](#configure-git)
    - [Code Guidelines](#code-guidelines)
    - [Author Commits](#author-commits)
    - [Submit Changes](#submit-changes)
  - [Build from Source](#build-from-source)
  - [Get Help](#get-help)

</details>

## Request Feature

You can request a feature through [GitHub Issues][action-request-feature].

## Report Vulnerability

To report security vulnerabilities, follow the directions in the [security policy](SECURITY.md).

## Report Bugs

You can report a bug through [GitHub Issues][action-report-bug].

## Make Changes

To minimize the amount of time and effort you put in, reach out to talk about your proposed changes
on the issue you wish to work on, or you can open a new [issue][issue-tracker] if no applicable
issue exists.

For any non-trivial change please explain:

- The use-case and how the change will help improve the project.
- What the API will look like.
- What could go wrong and if there are any special considerations.
- How it will be implemented (roughly).

### Pre-requisites

In order to make code contributions, you'll need:

- Install [Git][lnk-git].
- Get a [GitHub account][lnk-gh-join].
- Install [JDK][lnk-java-jdk] 11.
- Install your favourite IDE.

### Configure Git

Before you make your first commit, make sure it's configured.

Your global `.gitignore` file should contain your environment specific entries.
Such entries typically fall into one of these categories:

- **OS specific**; e.g. `*~`, `.DS_Store`, `.netrwhist`.
- **IDE specific**; e.g. `.vscode`, `.idea/`, `.metadata`, `*.sublime-*`, `.*.sw?`.
- **Tool specific**; e.g. entries specific to tools such as `asdf`, `sdkman`, etc.

Let us know who you are:

```shell
git config --global user.name <your name>
git config --global user.email <your-email@example.com>
```

Optional, but recommended, sign your git commits.
To generate an ssh-key you can follow:
[GitHub: Generate SSH Key][docs-gh-gen-ssh-key],
[GitHub: Add SSH Key to GH][docs-gh-add-ssh-key]
and [GitHub: Sign Commits][docs-gh-sign-commit].

TL;DR:

```shell
ssh-keygen -t ed25519 -C "your-email@example.com"
ssh-add ~/.ssh/id_ed25519
git config --global gpg.format ssh
git config --global user.signingkey ~/.ssh/id_ed25519.pub
git config --global commit.gpgsign true
git config --global tag.gpgsign true
# assumes GitHub's CLI tool 'gh' is installed (https://cli.github.com/)
gh ssh-key add ~/.ssh/id_ed25519.pub --title "<description>" --type signing
```

### Code Guidelines

Code contributions should contain the following:

- Unit test coverage for any logic introduced.
- Integration test coverage at the level of build execution.
- Documentation for the end-user if applicable.

Code formatting rules are enforced during build execution.
If formatting issues appear you can fix them automatically by executing `./gradlew spotlessApply`.
If you'd like to run the checks manually you can execute `./gradlew spotlessCheck`.

### Author Commits

Keep commits discrete and self-contained:

- Avoid including multiple unrelated changes in a single commit.
- Avoid spreading a single change across multiple commits.
  A single commit should make sense in isolation.

In order for your contributions to be accepted you must [sign off][docs-commit-signoff] your Git
commits to indicate you agree to the terms of [Developer Certificate of Origin][lnk-dev-cert].

**Writing Commit Messages:**

- Capitalize the subject line
- Use the imperative mood in the subject line
- Limit the subject line to 52 characters
- Do not end the subject line with a period
- Separate subject from body with a blank line
- Use a hanging indent for paragraphs
- Wrap the body at 72 characters
- Use the body to explain _what_ and _why_, not _how_

**Example imperative verbs**:

_Add, Remove, Fix, Enable, Disable, Format, Migrate, Upgrade, Extract_.

### Submit Changes

When you submit your pull request, someone will review it. It's normal that this process
can take some time, so don't get discouraged by any change requests.

## Build from Source

The project can be built from the command line using the included Gradle Wrapper scripts
`./gradlew build` or `./gradlew.bat build`.

## Get Help

If you run into any trouble, please reach out on the issue you are working on or open a new one.

[action-report-bug]: https://github.com/rognan/deno-gradle-plugin/issues/new?template=report-bug.md
[action-request-feature]: https://github.com/rognan/deno-gradle-plugin/issues/new?template=request-feature.md
[docs-commit-signoff]: https://git-scm.com/docs/git-commit#Documentation/git-commit.txt---signoff
[docs-gh-add-ssh-key]: https://docs.github.com/en/authentication/connecting-to-github-with-ssh/adding-a-new-ssh-key-to-your-github-account
[docs-gh-gen-ssh-key]: https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent
[docs-gh-sign-commit]: https://docs.github.com/en/authentication/managing-commit-signature-verification/signing-commits
[issue-tracker]: https://github.com/rognan/deno-gradle-plugin/issues
[lnk-dev-cert]: https://developercertificate.org/
[lnk-editorconfig]: https://editorconfig.org/
[lnk-git]: https://git-scm.com/
[lnk-gh-join]: https://github.com/join
[lnk-intellij-idea]: https://www.jetbrains.com/idea/
[lnk-java-jdk]: http://jdk.java.net/
