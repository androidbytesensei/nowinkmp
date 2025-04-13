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

package com.google.samples.apps.nowinandroid.shared.di

import com.google.samples.apps.nowinandroid.core.analytics.di.analyticsModule
import com.google.samples.apps.nowinandroid.core.data.di.dataModule
import com.google.samples.apps.nowinandroid.core.database.di.databaseModule
import com.google.samples.apps.nowinandroid.core.datastore.di.dataStoreModule
import com.google.samples.apps.nowinandroid.core.network.di.coroutineScopesModule
import com.google.samples.apps.nowinandroid.feature.bookmarks.bookmarksModule
import com.google.samples.apps.nowinandroid.feature.foryou.forYouModule
import com.google.samples.apps.nowinandroid.feature.interests.interestsModule
import com.google.samples.apps.nowinandroid.feature.search.searchModule
import com.google.samples.apps.nowinandroid.feature.settings.settingsModule
import com.google.samples.apps.nowinandroid.feature.topic.TopicModule
import com.google.samples.apps.nowinandroid.sync.di.syncModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinApplication
import org.koin.dsl.module

private val featureModules = module {
    includes(
        forYouModule,
        interestsModule,
        TopicModule,
        bookmarksModule,
        searchModule,
        settingsModule,
    )
}

val sharedAppModules = module {
    includes(
        coroutineScopesModule,
        sharedModule,
        dataModule,
        syncModule,
        analyticsModule,
        dataStoreModule,
        databaseModule,
        featureModules,
    )
}

fun koinConfiguration() = koinApplication {
    modules(sharedAppModules)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedAppModules)
    }
}