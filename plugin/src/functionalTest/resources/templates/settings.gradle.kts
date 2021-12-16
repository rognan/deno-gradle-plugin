buildCache {

  // Having the local cache in a temporary directory will ensure a clean build cache between
  // tests. The default local cache dir (A Gradle user home created by the Gradle Test Kit)
  // is re-used between tests.

  local {
    directory = file("%s")
  }
}
