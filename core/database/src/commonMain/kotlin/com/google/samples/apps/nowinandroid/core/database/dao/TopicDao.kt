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

package com.google.samples.apps.nowinandroid.core.database.dao

import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [TopicEntity] access
 */
interface TopicDao {
    fun getTopicEntity(topicId: String): Flow<TopicEntity>

    fun getTopicEntities(): Flow<List<TopicEntity>>

    suspend fun getOneOffTopicEntities(): List<TopicEntity>

    fun getTopicEntities(ids: Set<String>): Flow<List<TopicEntity>>

    /**
     * Inserts [topicEntities] into the db if they don't exist, and ignores those that do
     */
    suspend fun insertOrIgnoreTopics(topicEntities: List<TopicEntity>): List<Long>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    suspend fun upsertTopics(entities: List<TopicEntity>)

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    suspend fun deleteTopics(ids: List<String>)
}
