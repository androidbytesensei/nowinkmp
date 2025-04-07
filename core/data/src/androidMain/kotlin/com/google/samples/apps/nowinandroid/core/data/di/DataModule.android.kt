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

package com.google.samples.apps.nowinandroid.core.data.di

import com.google.samples.apps.nowinandroid.core.data.util.ConnectivityManagerNetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneBroadcastMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers
import com.google.samples.apps.nowinandroid.core.network.asQualifier
import com.google.samples.apps.nowinandroid.core.network.di.coroutineScopesModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val networkMonitorModule: Module
    get() = module {
        // Android specific implementation of NetworkMonitor
        single<CoroutineDispatcher> { Dispatchers.IO }

        single<NetworkMonitor> {
            ConnectivityManagerNetworkMonitor(
                context = androidContext(),
                ioDispatcher = get(),
            )
        }
    }

internal actual val timeZoneMonitorModule: Module
    get() = module {
        includes(coroutineScopesModule)
        
        // Android specific implementation of TimeZoneMonitor
        single<TimeZoneMonitor> {
            TimeZoneBroadcastMonitor(
                context = androidContext(),
                appScope = get(named("ApplicationScope")),
                ioDispatcher = get(NiaDispatchers.IO.asQualifier),
            )
        }
    }