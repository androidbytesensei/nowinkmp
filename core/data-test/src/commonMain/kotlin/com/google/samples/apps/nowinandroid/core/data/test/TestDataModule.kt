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

package com.google.samples.apps.nowinandroid.core.data.test

import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.RecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeNewsRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeRecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeSearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeTopicsRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeUserDataRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers
import com.google.samples.apps.nowinandroid.core.network.asQualifier
import com.google.samples.apps.nowinandroid.core.network.demo.DemoNiaNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin module for providing test implementations of repositories and other dependencies.
 * This module is used in the test environment to replace the production implementations
 * with fake or mock implementations.
 */
val testDataModule = module {
    // Provide test dispatchers
    single<CoroutineDispatcher> { get(NiaDispatchers.IO.asQualifier) }

    // Provide the DemoNiaNetworkDataSource needed by some fake repositories
    single {
        DemoNiaNetworkDataSource(
            ioDispatcher = get(NiaDispatchers.IO.asQualifier),
            networkJson = get(),
        )
    }

    singleOf(::AlwaysOnlineNetworkMonitor) bind NetworkMonitor::class
    singleOf(::DefaultZoneIdTimeZoneMonitor) bind TimeZoneMonitor::class
    singleOf(::FakeSearchContentsRepository) bind SearchContentsRepository::class
    singleOf(::FakeRecentSearchRepository) bind RecentSearchRepository::class
    singleOf(::FakeNewsRepository) bind NewsRepository::class
    singleOf(::FakeTopicsRepository) bind TopicsRepository::class
    singleOf(::FakeUserDataRepository) bind UserDataRepository::class
}
