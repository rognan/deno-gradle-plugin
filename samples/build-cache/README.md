# Hello, Build Cache!

This sample utilizes the gradle build cache for the `deno bundle` command.

Confirm the cache is working properly by running this command twice:

```shell
./gradlew clean bundle --info
```

You should se the following in the gradle output for the task's second run:

```shell
> Task :bundle FROM-CACHE
```
