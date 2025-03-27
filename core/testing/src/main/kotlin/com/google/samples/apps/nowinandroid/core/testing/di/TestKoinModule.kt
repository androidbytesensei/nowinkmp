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

package com.google.samples.apps.nowinandroid.core.testing.di

import com.google.samples.apps.nowinandroid.core.analytics.AnalyticsHelper
import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.RecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.SyncManager
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers
import com.google.samples.apps.nowinandroid.core.notifications.Notifier
import com.google.samples.apps.nowinandroid.core.testing.notifications.TestNotifier
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestRecentSearchRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestSearchContentsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.TestAnalyticsHelper
import com.google.samples.apps.nowinandroid.core.testing.util.TestNetworkMonitor
import com.google.samples.apps.nowinandroid.core.testing.util.TestSyncManager
import com.google.samples.apps.nowinandroid.core.testing.util.TestTimeZoneMonitor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module for providing test implementations
 */
val testModule = module {
    // Provide test dispatcher
    single<TestDispatcher> { UnconfinedTestDispatcher() }

    // Provide IO and Default dispatchers as the test dispatcher
    single<CoroutineDispatcher>(named(NiaDispatchers.IO.name)) { get<TestDispatcher>() }
    single<CoroutineDispatcher>(named(NiaDispatchers.Default.name)) { get<TestDispatcher>() }

    // Provide test repositories
    singleOf(::TestNewsRepository) { bind<NewsRepository>() }
    singleOf(::TestTopicsRepository) { bind<TopicsRepository>() }
    singleOf(::TestUserDataRepository) { bind<UserDataRepository>() }
    singleOf(::TestRecentSearchRepository) { bind<RecentSearchRepository>() }
    singleOf(::TestSearchContentsRepository) { bind<SearchContentsRepository>() }

    singleOf(::TestAnalyticsHelper) { bind<AnalyticsHelper>() }
    singleOf(::TestNetworkMonitor) { bind<NetworkMonitor>() }
    singleOf(::TestSyncManager) { bind<SyncManager>() }
    singleOf(::TestTimeZoneMonitor) { bind<TimeZoneMonitor>() }
    singleOf(::TestNotifier) { bind<Notifier>() }
}