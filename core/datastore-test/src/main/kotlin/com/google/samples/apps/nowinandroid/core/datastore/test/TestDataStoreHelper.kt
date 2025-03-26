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

package com.google.samples.apps.nowinandroid.core.datastore.test

import androidx.datastore.core.DataStore
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferences
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.test.KoinTest

/**
 * Helper class for working with DataStore in tests.
 */
object TestDataStoreHelper : KoinTest {
    /**
     * Loads the test DataStore module, overriding the regular DataStore module.
     * This should be called in the setUp method of tests.
     *
     * @return The loaded module, which should be unloaded in tearDown.
     */
    fun loadTestDataStoreModule(): Module {
        loadKoinModules(testDataStoreModule)
        return testDataStoreModule
    }

    /**
     * Unloads the test DataStore module.
     * This should be called in the tearDown method of tests.
     *
     * @param module The module returned by loadTestDataStoreModule.
     */
    fun unloadTestDataStoreModule(module: Module) {
        unloadKoinModules(module)
    }

    /**
     * Gets the in-memory DataStore instance for direct manipulation in tests.
     *
     * @return The DataStore instance.
     */
    fun getTestDataStore(): DataStore<UserPreferences> {
        return getKoin().get()
    }
}