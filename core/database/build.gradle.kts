/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.nowinandroid.kotlin.multiplatform.library)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.nowinandroid.sqldelight)
    alias(libs.plugins.nowinandroid.kotlin.multiplatform.koin)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.core.database"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
            implementation(projects.core.common)

            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.test.core)
            implementation(libs.androidx.test.runner)
            implementation(libs.kotlinx.coroutines.test)
        }

        jsMain.dependencies {
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.2"))
            implementation(npm("sql.js", "1.8.0"))
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
        }
    }
}

// Workaround yarn concurrency issue - https://youtrack.jetbrains.com/issue/KT-43320
tasks.withType<org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask>()
    .configureEach {
        args.addAll(
            listOf(
                "--mutex",
                "file:${file("../build/.yarn-mutex")}",
            ),
        )
    }