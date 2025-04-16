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

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDaoImpl
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceFtsDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceFtsDaoImpl
import com.google.samples.apps.nowinandroid.core.database.dao.RecentSearchQueryDao
import com.google.samples.apps.nowinandroid.core.database.dao.RecentSearchQueryDaoImpl
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDaoImpl
import com.google.samples.apps.nowinandroid.core.database.dao.TopicFtsDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicFtsDaoImpl
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers
import com.google.samples.apps.nowinandroid.core.network.asQualifier
import com.google.samples.apps.nowinandroid.core.network.di.dispatchersModule
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val schemaModule = module {
    single<SqlSchema<QueryResult.AsyncValue<Unit>>> { NiaDatabase.Schema }
}

internal val dbModule = module {
    single<NiaDatabase>{ NiaDatabase(get(SqlDriver::class)) }
}

val daosModule = module {
    includes(dispatchersModule, dbModule)
    single<CoroutineDispatcher> { get(NiaDispatchers.IO.asQualifier) }

    singleOf(::TopicDaoImpl) bind TopicDao::class
    singleOf(::NewsResourceDaoImpl) bind NewsResourceDao::class
    singleOf(::TopicFtsDaoImpl) bind TopicFtsDao::class
    singleOf(::NewsResourceFtsDaoImpl) bind NewsResourceFtsDao::class
    singleOf(::RecentSearchQueryDaoImpl) bind RecentSearchQueryDao::class
}

val databaseModule = module {
    includes(
        driverModule,
        dbModule,
        schemaModule,
        daosModule,
    )
}

internal expect val driverModule: Module