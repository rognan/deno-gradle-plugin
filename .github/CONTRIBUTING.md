# Contributing

This document explains how to:

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

If you've found a security vulnerability, please **do not** disclose it publicly.

Instead, follow the directions in the [security policy](SECURITY.md)

## Suggesting Features

[GitHub issues](https://github.com/rognan/deno-gradle-plugin/issues) is used to track new features.

When suggesting a feature, please include a description of the problem you need solved and
any requirements. If you have a suggestion on how to solve the issue, feel free to include
it in the description.

## Making Changes

To help minimize the amount of time and effort you put into it, please reach out to talk
about your proposed changes by opening an [issue](https://github.com/rognan/deno-gradle-plugin/issues).


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
- Your favorite text editor or IDE

## Configuring Git

Before making your first commit you'll need to configure `git`:

```shell
git config --global user.name <your full name>
git config --global user.email <your@email.com>
```

These commands edit the global git config. If you wish to configure your local git
repository config instead, navigate to the project root directory where you have your
`.git/` folder, and omit the flag `--global` before running the commands.

Optional, but recommended, you can enable signing your git commits like this:

```shell
git config --global user.signingkey <key id>
git config --global commit.gpgsign true
```

For commit signing to work as intended you'll need to install and configure GPG or S/MIME.

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

Add default key to the `gpg.conf` and `gpg-agent.conf` found in your `~/.gnupg/` folder on
your local machine. Afterwards you may need to reload your shell and your gpg-agent.

Follow the [official GitHub documentation](https://help.github.com/en/articles/adding-a-new-gpg-key-to-your-github-account)
to upload the public part of your gpg key to GitHub.

Use `git verify-commit <commit hash>` to verify your very first commit.
</details>

<details>
  <summary>A note about ~/.gitignore</summary>

You may use whichever OS or IDE you wish, just add your development environment specific
files to your global `~/.gitignore` file first.

Such files typically fall into these categories:

- **Operating System specific files**; i.e. `*~`, `.DS_Store`, `.netrwhist`.
- **IDE specific files**; i.e. `.idea/`, `.metadata`, `*.sublime-*`, `.*.sw?`.
- **Tooling specific files**; i.e. for `jenv`, `asdf`, `sdkman`, etc.
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

## Authoring Commits

Keep commits discrete and self-contained:
- Avoid including multiple unrelated changes in a single commit
- Avoid spreading a single change across multiple commits.
  A single commit should make sense in isolation

In order for your contributions to be accepted, you must [sign off](https://git-scm.com/docs/git-commit#Documentation/git-commit.txt---signoff) your Git commits to
indicate you agree to the terms of [Developer Certificate of Origin](https://developercertificate.org/).

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

The project can be built from the command line using the included Gradle Wrapper scripts
`./gradlew` or `./gradlew.bat`.

## Submitting Changes

When you submit your pull request, someone will review it. It's normal that this process
can take some time, so don't get discouraged by any change requests.

## Getting Help

If you run into any trouble, please reach out on the issue you are working on.
