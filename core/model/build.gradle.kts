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
}

android {
    namespace = "com.google.samples.apps.nowinandroid.core.model.data"
}

kotlin {
    // Configure source sets
    sourceSets {
        // Common source set - shared across all platforms
        commonMain.dependencies {
            // Common dependencies
            implementation(libs.kotlinx.datetime)
        }
    }
}

// This block will be removed while cleanup,
// we're adding this to ensure this library imports are available on android modules
dependencies {
    implementation(libs.kotlinx.datetime)
}