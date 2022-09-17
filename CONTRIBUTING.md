# Contributing

This project is Open Source Software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
If you'd like to contribute, or simply want a go at the code, this document should help you get started.

The rest of this document explains how to:

- contribute to this project
- configure your development environment
- work on the code base
- maximize the chance of your changes to be accepted
- get help if you encounter trouble

## Reporting Bugs

[GitHub issues](https://github.com/rognan/deno-gradle-plugin/issues) is used to track bugs.

If you're reporting a bug, please provide as much information about your build as possible.
A small sample project, or a specific guide on how to reproduce the bug, would be ideal.

## Reporting Security Vulnerabilities

If you think you've found a security vulnerability in this project, please **DO NOT** disclose it
publicly. Instead, contact a maintainer privately.

It'd be a great help if you can include details on how the issue being reported can be duplicated.

## Suggesting Features

[GitHub issues](https://github.com/rognan/deno-gradle-plugin/issues) is used to track new features. When suggesting a feature, please include a description
of the problem you need solved and any requirements. If you have a suggestion on how to solve the
issue, please include it in the description.

## Making Changes

Before spending time coding and submitting a pull request, please reach out to talk about your
proposed changes beforehand by opening an [issue](https://github.com/rognan/deno-gradle-plugin/issues).
This will help minimize the amount of effort and time you put into it.

For any non-trivial change, create a short design document explaining:

- Why the change? What's the use case?
- What will the API look like?
- What test cases should it have? What could go wrong?
- How will it roughly be implemented?

This can be done directly inside the GitHub issue.

## Development Setup

In order to make changes, you'll need:

- A [JDK](http://jdk.java.net/), version 11.
- [Git](https://git-scm.com/), and a [GitHub account](https://github.com/join).
- A text editor or Integrated Development Environment (IDE) like [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## Configuring Git

Before you make your first commit you'll need to configure `git`:

```shell
git config --global user.name <your full name>
git config --global user.email <your@email.com>
```

These commands edit the global git config. If you wish to configure your local git repository
config instead, navigate to the project root directory where you have your `.git/` folder, and omit
the flag `--global` before running the commands.

Optionally, but highly recommended, you can enable signing your git commits.

```shell
git config --global user.signingkey <key id>
git config --global commit.gpgsign true
```

For git commit signing to work as intended you'll need to install and configure GPG or S/MIME.

<details>
  <summary>Install and configure GPG</summary>

## Installing GPG (on MacOs)

**Using homebrew**:

```shell
brew install gnupg pinentry-mac
gpg --full-generate-key
# Follow the wizard (Opt for RSA/4096)
gpg --list-secret-keys --keyid-format LONG
# Copy key (I.e. the last part of `rsa4096/AC23B8C17ECEED2F`)
git config --global gpg.program gpg
echo "export GPG_TTY=$(tty)" >> ~/.bashrc
```

Add default key to the `gpg.conf` and `gpg-agent.conf` found in your `~/.gnupg/` folder on your
local machine. Afterwards you may need to reload your shell and your gpg-agent.

Follow the [official GitHub documentation](https://help.github.com/en/articles/adding-a-new-gpg-key-to-your-github-account)
to upload the public part of your gpg key to GitHub.

Use `git verify-commit <commit hash>` to verify your very first commit.
</details>

<details>
  <summary>A note about ~/.gitignore</summary>

You may use whichever OS or IDE you wish, just remember to add your development environment specific
files to your global `~/.gitignore` file.

These files typically fall into these categories:

- **Operating System specific files**; such as `*~`, `.DS_Store`, `.netrwhist`, etc.
- **IDE specific files**; such as `.idea/`, `.metadata`, `*.sublime-*`, `.*.sw?`, etc.
- **Non-project tooling specific files**; for tools such as `jenv`, `asdf`, `sdkman`, etc
</details>

## Importing into IntelliJ IDEA

To import the project in [IntelliJ IDEA](https://www.jetbrains.com/idea/), v2020.3, or newer, is
required:

- Use _"File"_ → _"Open"_, and select the `build.gradle.kts` file to import the code.
- Ensure _"Use default gradle wrapper"_ is selected.
- In _"Gradle Settings"_ (_"Preferences"_ → _"Build Tools"_ → _"Gradle"_)
  - Ensure _"Build and run using"_ and _"Run tests using"_ are set to Gradle (Default)
  - Select a Java 11 VM as "Gradle JVM"
- In the _"File already exists"_ dialogue, choose _"Yes"_ to overwrite.
- In the _"Open Project"_ dialogue, choose _"Delete Existing Project and Import"_.

## Importing into Other IDEs

Please refer to your vendor documentation.

## Code Change Guidelines

All code contributions should contain the following:

- Unit tests for any logic introduced
- Integration test coverage of the bug/feature at the level of build execution.
- Documentation

The CI will verify that your code runs on all supported Java versions and operating systems.

## Authoring Commits

- Keep commits discrete: avoid including multiple unrelated changes in a single commit
- Keep commits self-contained: avoid spreading a single change across multiple commits.
  A single commit should make sense in isolation

In order for your contributions to be accepted, you must [sign off](https://git-scm.com/docs/git-commit#Documentation/git-commit.txt---signoff)
your Git commits to indicate that you agree to the terms of [Developer Certificate of Origin](https://developercertificate.org/).

## Writing Commit Messages

- Capitalize the subject line
- Use the imperative mood in the subject line
- Limit the subject line to about 50 characters
- Do not end the subject line with a period
- Separate subject from body with a blank line
- Use a hanging indent for paragraphs
- Wrap the body at 72 characters
- Use the body to explain _what_ and _why_, not _how_
- End the commit message with the issue id

**Example imperative verbs**:

_Add, Remove, Fix, Enable, Disable, Format, Migrate, Upgrade, Extract_.

## Building from Source

The project should be built using a JDK version 11, and can be built from the CLI using the included
Gradle Wrapper scripts (`./gradlew` or `./gradlew.bat`)

## Submitting Changes

After you submit your pull request, someone will review it. It's normal that this process can take
some time, so don't get discouraged by any change requests.

## Getting Help

If you run into any trouble, please reach out on the issue you are working on.
