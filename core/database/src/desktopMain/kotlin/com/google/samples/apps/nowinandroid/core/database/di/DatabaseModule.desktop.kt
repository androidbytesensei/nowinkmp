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

package com.google.samples.apps.nowinandroid.core.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.Properties

internal actual val driverModule: Module
    get() = module {
        single<SqlDriver> {
            JdbcSqliteDriver(
                url = "jdbc:sqlite:nia-database.db",
                properties = Properties().apply { put("foreign_keys", "true") },
            ).also {
                NiaDatabase.Schema.create(it)
            }
        }
    }