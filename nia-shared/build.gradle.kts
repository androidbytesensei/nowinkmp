/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-wallet/blob/master/LICENSE.md
 */

plugins {
    alias(libs.plugins.nowinandroid.kotlin.multiplatform.library)
    alias(libs.plugins.nowinandroid.compose.multiplatform.feature)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            optimized = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.analytics)
            implementation(projects.core.network)
            implementation(projects.core.notifications)

            //put your multiplatform dependencies here
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.materialIconsExtended)
            implementation(compose.foundation)
            implementation(libs.jb.material3.adaptive)
            implementation(libs.jb.material3.adaptive.layout)
            implementation(libs.jb.material3.adaptive.navigation)
            implementation(libs.back.handler)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(projects.feature.interests)
            implementation(projects.feature.foryou)
            implementation(projects.feature.bookmarks)
            implementation(projects.feature.topic)
            implementation(projects.feature.search)
            implementation(projects.feature.settings)

            implementation(projects.sync.work)
        }

        androidMain.dependencies {
            implementation(libs.androidx.profileinstaller)
            implementation(libs.kotlinx.coroutines.guava)
        }

        desktopMain.dependencies {
            // Desktop specific dependencies
            implementation(compose.desktop.currentOs)
            implementation(compose.desktop.common)
        }
    }
}

android {
    namespace = "com.google.samples.apps.nowinandroid.shared"

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "shared.generated.resources"
}