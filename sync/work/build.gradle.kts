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
    alias(libs.plugins.nowinandroid.kotlin.multiplatform.koin)
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
    }
    namespace = "com.google.samples.apps.nowinandroid.sync"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.analytics)
            implementation(projects.core.data)
            implementation(projects.core.notifications)

            implementation(libs.koin.core)
            implementation(libs.kermit.logging)
        }

        androidMain.dependencies {
            implementation(libs.androidx.tracing.ktx)
            implementation(libs.androidx.work.ktx)
            implementation(libs.koin.androidx.workmanager)


            implementation(libs.firebase.cloud.messaging)
            implementation(project.dependencies.platform(libs.firebase.bom))
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.androidx.work.testing)
            implementation(libs.koin.android.test)
            implementation(libs.kotlinx.coroutines.guava)
            implementation(projects.core.testing)
            implementation(libs.androidx.test.monitor)
        }
    }
}
