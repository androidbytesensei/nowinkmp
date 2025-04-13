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

package com.google.samples.apps.nowinandroid.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.analytics.AnalyticsHelper
import com.google.samples.apps.nowinandroid.core.analytics.LocalAnalyticsHelper
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.ui.LocalTimeZone
import com.google.samples.apps.nowinandroid.shared.ui.NiaApp
import com.google.samples.apps.nowinandroid.shared.ui.rememberNiaAppState
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NiaSharedApp(
    modifier: Modifier = Modifier,
    networkMonitor: NetworkMonitor = koinInject(),
    timeZoneMonitor: TimeZoneMonitor = koinInject(),
    analyticsHelper: AnalyticsHelper = koinInject(),
    userNewsResourceRepository: UserNewsResourceRepository = koinInject(),
) {
    SharedApp(
        modifier = modifier,
        networkMonitor = networkMonitor,
        timeZoneMonitor = timeZoneMonitor,
        analyticsHelper = analyticsHelper,
        userNewsResourceRepository = userNewsResourceRepository
    )
}

@Composable
private fun SharedApp(
    modifier: Modifier = Modifier,
    networkMonitor: NetworkMonitor,
    timeZoneMonitor: TimeZoneMonitor,
    analyticsHelper: AnalyticsHelper,
    userNewsResourceRepository: UserNewsResourceRepository,
    viewModel: NiaSharedAppViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val appState = rememberNiaAppState(
        networkMonitor = networkMonitor,
        userNewsResourceRepository = userNewsResourceRepository,
        timeZoneMonitor = timeZoneMonitor,
    )

    val currentTimeZone by appState.currentTimeZone.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalAnalyticsHelper provides analyticsHelper,
        LocalTimeZone provides currentTimeZone,
    ) {
        NiaTheme(
            darkTheme = uiState.shouldUseDarkTheme(isSystemInDarkTheme),
            androidTheme = uiState.shouldUseAndroidTheme,
            disableDynamicTheming = uiState.shouldDisableDynamicTheming,
        ) {
            NiaApp(
                appState = appState,
                modifier = modifier
            )
        }
    }
}