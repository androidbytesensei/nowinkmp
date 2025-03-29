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

package com.google.samples.apps.nowinandroid.core.database.mapper

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Generic adapter for SQLDelight database operations.
 * Handles the mapping between database types and domain types.
 */
class SQLDelightAdapter<DB : Any, Domain : Any>(
    private val entityMapper: EntityMapper<DB, Domain>,
    private val dispatcher: CoroutineDispatcher
) {
    /**
     * Maps a query result to a Flow of a single domain entity.
     */
    fun Query<DB>.asDomainModel(): Flow<Domain> {
        return this.asFlow()
            .mapToOne(dispatcher)
            .map { entityMapper.mapToDomain(it) }
    }

    /**
     * Maps a query result to a Flow of a nullable domain entity.
     */
    fun Query<DB>.asDomainModelOrNull(): Flow<Domain?> {
        return this.asFlow()
            .mapToOneOrNull(dispatcher)
            .map { it?.let { entityMapper.mapToDomain(it) } }
    }

    /**
     * Maps a query result to a Flow of domain entity lists.
     */
    fun Query<DB>.asDomainModelList(): Flow<List<Domain>> {
        return this.asFlow()
            .mapToList(dispatcher)
            .map { list -> list.map { entityMapper.mapToDomain(it) } }
    }

    /**
     * Executes a query and returns a list of domain entities synchronously.
     */
    suspend fun Query<DB>.executeAsDomainModelList(): List<Domain> = withContext(dispatcher) {
        this@executeAsDomainModelList.executeAsList().map { entityMapper.mapToDomain(it) }
    }

    /**
     * Executes a query and returns a single domain entity synchronously.
     */
    suspend fun Query<DB>.executeAsDomainModel(): Domain = withContext(dispatcher) {
        this@executeAsDomainModel.executeAsOne().let { entityMapper.mapToDomain(it) }
    }

    /**
     * Executes a query and returns a nullable domain entity synchronously.
     */
    suspend fun Query<DB>.executeAsDomainModelOrNull(): Domain? = withContext(dispatcher) {
        this@executeAsDomainModelOrNull.executeAsOneOrNull()?.let { entityMapper.mapToDomain(it) }
    }
}