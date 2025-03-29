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

import app.cash.sqldelight.gradle.SqlDelightExtension
import com.google.samples.apps.nowinandroid.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

private const val DATABASE_NAME = "NiaDatabase"

class SQLDelightConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply the SQLDelight plugin
            apply(plugin = "app.cash.sqldelight")

            // Configure SQLDelight extension
            extensions.configure<SqlDelightExtension> {
                // Define the database
                databases.create(DATABASE_NAME){
                    // Package where generated Kotlin code will be placed
                    packageName.set("com.google.samples.apps.nowinandroid.core.database")
                    // Enable migration validation
                    verifyMigrations.set(true)
                    dialect("app.cash.sqldelight:sqlite-3-38-dialect:2.0.2")
                    generateAsync.set(true)
                }
                linkSqlite.set(true)
            }

            // Add required dependencies
            dependencies {
                // SQLDelight base dependencies
                add("implementation", libs.findLibrary("sqldelight.android.driver").get())
                add("implementation", libs.findLibrary("sqldelight.coroutines.extensions").get())
                add("implementation", libs.findLibrary("sqldelight.primitive.adapters").get())

                // Testing dependencies
                add("testImplementation", libs.findLibrary("sqldelight.sqlite.driver").get())
            }
        }
    }
}