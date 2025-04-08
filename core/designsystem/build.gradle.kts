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
    alias(libs.plugins.nowinandroid.kotlin.multiplatform.koin)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.core.designsystem"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.navigation)

            api(compose.ui)
            api(compose.uiUtil)
            api(compose.runtime)
            api(compose.material3)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(compose.material3AdaptiveNavigationSuite)
            api(compose.materialIconsExtended)
            api(compose.foundation)
            api(libs.jb.material3.adaptive)
            api(libs.jb.material3.adaptive.layout)
            api(libs.coil.compose)
        }

        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

        }
        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.compose.ui.test)
            implementation(projects.core.testing)
        }
        androidUnitTest.dependencies {
            implementation(libs.androidx.compose.ui.test)
            implementation(libs.androidx.compose.ui.testManifest)
            implementation(libs.hilt.android.testing)
            implementation(libs.robolectric)
            implementation(libs.roborazzi)
            implementation(projects.core.screenshotTesting)
            implementation(projects.core.testing)
        }
    }
}

dependencies {
    lintPublish(projects.lint)
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "designsystem.generated.resources"
}
