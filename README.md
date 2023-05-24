# KtGitHub - Kotlin multiplatform library for accessing the GitHub API

![License](https://img.shields.io/github/license/russellbanks/KtGitHub)

# Get started

This library is in alpha and releases are only available form JitPack for the time being. They will be added to Maven Central once it is stable.

In your Gradle build file:

```gradle
repositories {
  maven("https://jitpack.io")
}
```

In your app Gradle build file:

[![](https://www.jitpack.io/v/russellbanks/KtGitHub.svg)](https://www.jitpack.io/#russellbanks/KtGitHub)

```kotlin
dependencies {
  implementation("com.github.russellbanks.KtGitHub:KtGitHub:<version>")
}
```

In a multiplatform project:

```kotlin
val commonMain by getting {
    dependencies {
        implementation("com.github.russellbanks.KtGitHub:KtGitHub:<version>") 
    }
}
```

You also need a [Ktor engine](https://ktor.io/docs/http-client-engines.html):

```kotlin
dependencies {
  implementation("io.ktor:ktor-client-<engine>:<version>")
}
```

In a multiplatform project, make sure to add the engine under each platform. For example:

```kotlin
val jvmMain by getting {
    dependencies {
        implementation("io.ktor:ktor-client-cio:<version>") 
    }
}
```

Create the GitHub object with your engine and an optional DSL builder:
```kotlin
val gitHub = GitHub.create(engine) // CIO, Java, JS, etc
```

```kotlin
val gitHub = GitHub.create(engine) {
    token = "GITHUB TOKEN"
}
```


## License

    Copyright 2023 Russell Banks

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
