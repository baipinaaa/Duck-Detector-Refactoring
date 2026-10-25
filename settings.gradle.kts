/*
 * Copyright 2026 Duck Apps Contributor
 * If you have any questions, suggestions, or other inquiries, please email Eltavine <me@eltavine.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
// The UI framework ships as a Git submodule upstream. `uihelper/` may also arrive as a plain
// directory (for example in an archive), where its own `.git` is missing: querying the version with
// `git rev-list` there would silently count this repository's commits instead, so fall back to the
// pinned publication version in that case.
if (!file("uihelper/settings.gradle.kts").isFile) {
    throw GradleException("The uihelper submodule is not checked out. Run: git submodule update --init --recursive")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            // Gradle already imports gradle/libs.versions.toml for the default catalog.
            // Match the checked-out submodule's Maven publication version automatically; a plain
            // directory checkout without git metadata uses the pinned commit count instead.
            val uihelperVersion = if (file("uihelper/.git").exists()) {
                providers.exec {
                    workingDir = file("uihelper")
                    commandLine("git", "rev-list", "--count", "HEAD")
                }.standardOutput.asText.map { it.trim() }.get()
            } else {
                "56"
            }
            library("uihelper", "io.github.xiaotong6666", "uihelper")
                .version(uihelperVersion)
        }
    }
}

rootProject.name = "Duck Detector"

// A module is a directory with a build file at its group's depth, and its Gradle path mirrors that
// directory, so adding a detector, capability or core module never needs an edit here.
fun includeModules(group: String, depth: Int) {
    var directories = listOf(rootDir.resolve(group))
    repeat(depth) {
        directories = directories.flatMap { directory ->
            directory.listFiles().orEmpty().filter { it.isDirectory }.sortedBy { it.name }
        }
    }
    directories
        .filter { it.resolve("build.gradle.kts").isFile }
        .forEach { include(":" + it.relativeTo(rootDir).invariantSeparatorsPath.replace('/', ':')) }
}

include(":app")
includeModules("sdk", depth = 1)
includeModules("feature", depth = 2)
includeModules("capability", depth = 2)
includeModules("core", depth = 1)

// Keep the UI framework as an independent Gradle build. Its own version catalog is authoritative;
// including it as a regular project would merge Duck's catalog into it.
includeBuild("uihelper")
