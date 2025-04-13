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

package com.google.samples.apps.nowinandroid

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.google.samples.apps.nowinandroid.di.niaAppModule
import com.google.samples.apps.nowinandroid.shared.di.sharedAppModules
import com.google.samples.apps.nowinandroid.shared.util.ProfileVerifierLogger
import com.google.samples.apps.nowinandroid.sync.initializers.Sync
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration

/**
 * [Application] class for NiA
 */
@OptIn(KoinExperimentalAPI::class)
class NiaApplication : Application(), KoinStartup, SingletonImageLoader.Factory {
    val profileVerifierLogger: ProfileVerifierLogger by inject()

    @KoinExperimentalAPI
    override fun onKoinStartup(): KoinConfiguration = koinConfiguration {
        androidContext(this@NiaApplication)
        androidLogger()
        workManagerFactory()
        modules(sharedAppModules + niaAppModule)
    }

    override fun onCreate() {
        super.onCreate()
//        setStrictModePolicy()

        // Initialize Sync; the system responsible for keeping data in the app up to date.
        Sync.initialize(context = this)
        profileVerifierLogger()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

    /**
     * Return true if the application is debuggable.
     */
    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    /**
     * Set a thread policy that detects all potential problems on the main thread, such as network
     * and disk access.
     *
     * If a problem is found, the offending call will be logged and the application will be killed.
     */
    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            StrictMode.setThreadPolicy(
                Builder().detectAll().penaltyLog().penaltyDeath().build(),
            )
        }
    }
}
