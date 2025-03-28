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

package com.google.samples.apps.nowinandroid.core.data.test

import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

/**
 * Helper class for loading test modules in tests.
 */
object TestModuleLoader {
    /**
     * Starts Koin with the test modules.
     * Call this in the @Before method of your test.
     */
    fun startTestKoin(additionalModules: List<Module> = emptyList()) {
        stopKoin() // Ensure no previous Koin instance is running
        startKoin {
            modules(listOf(testDataModule) + additionalModules)
        }
    }

    /**
     * Adds test modules to an existing Koin instance.
     * Use this if you want to keep the existing modules and just add the test ones.
     */
    fun loadTestModules(additionalModules: List<Module> = emptyList()) {
        loadKoinModules(listOf(testDataModule) + additionalModules)
    }
}
