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

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.mapper.NewsResourceMapper
import com.google.samples.apps.nowinandroid.core.database.mapper.SQLDelightAdapter
import com.google.samples.apps.nowinandroid.core.database.mapper.TopicsMapper
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceTopicCrossRef
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedNewsResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NewsResourceDaoImpl(
    private val db: NiaDatabase,
    private val dispatcher: CoroutineDispatcher,
) : NewsResourceDao {
    private val query = db.newsResourceQueries
    private val topicQuery = db.topicsQueries
    private val mapper = NewsResourceMapper()
    private val topicMapper = TopicsMapper()
    private val adapter = SQLDelightAdapter(mapper, dispatcher)

    override fun getNewsResources(
        useFilterTopicIds: Boolean,
        filterTopicIds: Set<String>,
        useFilterNewsIds: Boolean,
        filterNewsIds: Set<String>,
    ): Flow<List<PopulatedNewsResource>> {
        // Choose the right query based on the filters
        val newsResourcesFlow = when {
            useFilterNewsIds && useFilterTopicIds ->
                query.getNewsResourcesByIdsAndTopicIds(filterNewsIds, filterTopicIds)
                    .asFlow()
                    .mapToList(dispatcher)

            useFilterNewsIds ->
                query.getNewsResourcesByIds(filterNewsIds)
                    .asFlow()
                    .mapToList(dispatcher)

            useFilterTopicIds ->
                query.getNewsResourcesByTopicIds(filterTopicIds)
                    .asFlow()
                    .mapToList(dispatcher)

            else ->
                query.getAllNewsResources()
                    .asFlow()
                    .mapToList(dispatcher)
        }

        // Map to domain models with topics
        return newsResourcesFlow.map { newsResources ->
            newsResources.map { newsResource ->
                // Get topics for this news resource
                val topics = query.getTopicsForNewsResource(newsResource.id)
                    .executeAsList()
                    .map { topicMapper.mapToDomain(it) }

                PopulatedNewsResource(
                    entity = mapper.mapToDomain(newsResource),
                    topics = topics,
                )
            }
        }.flowOn(dispatcher)
    }

    override fun getNewsResourceIds(
        useFilterTopicIds: Boolean,
        filterTopicIds: Set<String>,
        useFilterNewsIds: Boolean,
        filterNewsIds: Set<String>,
    ): Flow<List<String>> {
        return query.getNewsResourceIds(
            useFilterNewsIds = useFilterNewsIds,
            filterNewsIds = filterNewsIds,
            useFilterTopicIds = useFilterTopicIds,
            filterTopicIds = filterTopicIds,
        ).asFlow().mapToList(dispatcher)
    }

    override suspend fun upsertNewsResources(newsResourceEntities: List<NewsResourceEntity>) {
        withContext(dispatcher) {
            newsResourceEntities.forEach { params ->
                // Insert or update the news resource
                query.upsertNewsResources(
                    id = params.id,
                    title = params.title,
                    content = params.content,
                    url = params.url,
                    header_image_url = params.headerImageUrl,
                    publish_date = params.publishDate.toEpochMilliseconds(),
                    type = params.type,
                )
            }
        }
    }

    override suspend fun insertOrIgnoreTopicCrossRefEntities(newsResourceTopicCrossReferences: List<NewsResourceTopicCrossRef>) {
        withContext(dispatcher) {
            newsResourceTopicCrossReferences.forEach { data ->
                query.insertOrIgnoreTopicCrossRefEntitiy(
                    news_resource_id = data.newsResourceId,
                    topic_id = data.topicId,
                )
            }
        }
    }

    override suspend fun deleteNewsResources(ids: List<String>) {
        withContext(dispatcher){
            query.deleteNewsResources(ids)
        }
    }
}