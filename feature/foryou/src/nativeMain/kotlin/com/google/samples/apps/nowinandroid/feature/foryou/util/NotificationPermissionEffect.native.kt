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

package com.google.samples.apps.nowinandroid.feature.foryou.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import dev.icerock.moko.permissions.Permission.REMOTE_NOTIFICATION
import dev.icerock.moko.permissions.PermissionState.DeniedAlways
import dev.icerock.moko.permissions.PermissionState.Granted
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

@Composable
internal actual fun NotificationPermissionEffect() {
    if (LocalInspectionMode.current) return

    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }

    LaunchedEffect(Unit) {
        val permissionState = controller.getPermissionState(REMOTE_NOTIFICATION)
        when (permissionState) {
            Granted -> {}

            DeniedAlways -> {
                controller.openAppSettings()
            }

            else -> {
                controller.providePermission(REMOTE_NOTIFICATION)
            }
        }
    }
}