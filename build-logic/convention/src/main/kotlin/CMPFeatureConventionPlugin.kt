/*
 * Copyright 2025 The Android Open Source Project
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

import com.google.samples.apps.nowinandroid.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CMPFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("nowinandroid.kotlin.multiplatform.library")
//                apply("org.convention.kmp.koin")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                add("commonMainImplementation", project(":core:ui"))
                add("commonMainImplementation", project(":core:designsystem"))
                add("commonMainImplementation", project(":core:data"))

                add(
                    "commonMainImplementation",
                    libs.findLibrary("kotlinx.serialization.json").get(),
                )

                // Remove this Koin Convention Plugin migrated to KMP
                add("commonMainImplementation", libs.findLibrary("koin.core").get())

                add("commonMainImplementation", libs.findLibrary("koin.compose").get())
                add("commonMainImplementation", libs.findLibrary("koin.compose.viewmodel").get())

                add("commonMainImplementation", libs.findLibrary("jb.composeRuntime").get())
                add("commonMainImplementation", libs.findLibrary("jb.composeViewmodel").get())
                add("commonMainImplementation", libs.findLibrary("jb.lifecycleViewmodel").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("jb.lifecycleViewmodelSavedState").get(),
                )
                add("commonMainImplementation", libs.findLibrary("jb.savedstate").get())
                add("commonMainImplementation", libs.findLibrary("jb.bundle").get())
                add("commonMainImplementation", libs.findLibrary("jb.composeNavigation").get())
            }
        }
    }
}