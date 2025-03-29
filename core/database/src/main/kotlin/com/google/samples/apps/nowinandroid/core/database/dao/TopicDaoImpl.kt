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

package com.google.samples.apps.nowinandroid.core.database.dao

import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.Topics
import com.google.samples.apps.nowinandroid.core.database.mapper.SQLDelightAdapter
import com.google.samples.apps.nowinandroid.core.database.mapper.TopicsMapper
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class TopicDaoImpl(
    db: NiaDatabase,
    private val ioDispatcher: CoroutineDispatcher,
) : TopicDao {
    private val query = db.topicsQueries
    private val mapper = TopicsMapper()
    private val adapter: SQLDelightAdapter<Topics, TopicEntity> =
        SQLDelightAdapter(mapper, ioDispatcher)

    override fun getTopicEntity(topicId: String): Flow<TopicEntity> {
        return with(adapter) {
            query.getTopicEntity(topicId).asDomainModel()
        }
    }

    override fun getTopicEntities(): Flow<List<TopicEntity>> {
        return with(adapter) {
            query.getAllTopics().asDomainModelList()
        }
    }

    override fun getTopicEntities(ids: Set<String>): Flow<List<TopicEntity>> {
        return with(adapter) {
            query.getTopicEntities(ids).asDomainModelList()
        }
    }

    override suspend fun getOneOffTopicEntities(): List<TopicEntity> {
        return with(adapter) {
            query.getOneOffTopicEntities().executeAsDomainModelList()
        }
    }

    override suspend fun insertOrIgnoreTopics(topicEntities: List<TopicEntity>): List<Long> {
        topicEntities.map {
            query.insertOrIgnoreTopics(
                id = it.id,
                name = it.name,
                short_description = it.shortDescription,
                long_description = it.longDescription,
                url = it.url,
                image_url = it.imageUrl,
            )
        }

        return topicEntities.mapNotNull { it.id.toLongOrNull() }
    }

    override suspend fun upsertTopics(entities: List<TopicEntity>) {
        entities.forEach {
            query.upsertTopics(
                id = it.id,
                name = it.name,
                short_description = it.shortDescription,
                long_description = it.longDescription,
                url = it.url,
                image_url = it.imageUrl,
            )
        }
    }

    override suspend fun deleteTopics(ids: List<String>) {
        query.deleteTopics(ids)
    }
}