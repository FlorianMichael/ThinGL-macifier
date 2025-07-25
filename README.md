# ThinGL-macifier

This is a fork of the original [ThinGL](https://github.com/RaphiMC/ThinGL) project to propose personal changes, mainly for private projects.

### Changes made to the project

* **Added macOS support** Lowers the OpenGL requirement from 4.5 to 4.1
* **New functions** 
  - `Renderer2D#outlinedGradientRectangle`

Changes in the source code are marked with comments for clarity

```java
// FlorianMichael - do change xy
```

An exception to this are the GL42C+ replaces inside the resource package. During merge conflicts, drop our changes and run `python gl_replacements.py`

-----

Run `python merge_upstream.py` to add upstream commits. Resolve merge conflicts and `git commit --amend` into the merge commit.

## Include via JitPack

I wouldn't necessarily recommend depending on this, but if needed

### 1. Add the JitPack repository

<details>
<summary><strong>Gradle (Kotlin DSL)</strong></summary>

```kotlin
repositories {
    maven("https://jitpack.io")
}
```

</details>

<details>
<summary><strong>Gradle (Groovy)</strong></summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

</details>

### 2. Add the dependency

Replace `VERSION` with a tag, release, or commit hash from this repository.

<details>
<summary><strong>Gradle (Kotlin DSL)</strong></summary>

```kotlin
dependencies {
    implementation("com.github.FlorianMichael:ThinGL-macifier:VERSION")
}
```

</details>

<details>
<summary><strong>Gradle (Groovy)</strong></summary>

```groovy
dependencies {
    implementation 'com.github.FlorianMichael:ThinGL-macifier:VERSION'
}
```

</details>
