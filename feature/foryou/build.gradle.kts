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
    alias(libs.plugins.nowinandroid.compose.multiplatform.feature)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.feature.foryou"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.notifications)

            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }

        nativeMain.dependencies {
            implementation(libs.moko.permission)
            implementation(libs.moko.permission.compose)
        }

        androidMain.dependencies {
            implementation(libs.moko.permission)
            implementation(libs.moko.permission.compose)
        }

        androidUnitTest.dependencies {
            implementation(libs.hilt.android.testing)
            implementation(libs.robolectric)
            implementation(projects.core.testing)
            implementation(projects.core.screenshotTesting)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.bundles.androidx.compose.ui.test)
            implementation(projects.core.testing)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "foryou.generated.resources"
}
