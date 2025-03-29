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
import com.google.samples.apps.nowinandroid.core.database.mapper.RecentSearchQueryMapper
import com.google.samples.apps.nowinandroid.core.database.mapper.SQLDelightAdapter
import com.google.samples.apps.nowinandroid.core.database.model.RecentSearchQueryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RecentSearchQueryDaoImpl(
    private val db: NiaDatabase,
    private val dispatcher: CoroutineDispatcher,
) : RecentSearchQueryDao {
    private val query = db.recentSearchQueryQueries
    private val mapper = RecentSearchQueryMapper()
    private val adapter = SQLDelightAdapter(mapper, dispatcher)
    override fun getRecentSearchQueryEntities(limit: Int): Flow<List<RecentSearchQueryEntity>> {
        return with(adapter) {
            query.getRecentSearchQueryEntities(limit.toLong()).asDomainModelList()
        }
    }

    override suspend fun insertOrReplaceRecentSearchQuery(recentSearchQuery: RecentSearchQueryEntity) {
        return withContext(dispatcher) {
            query.insertOrReplaceRecentSearchQuery(
                mapper.mapToEntity(recentSearchQuery),
            )
        }
    }

    override suspend fun clearRecentSearchQueries() {
        return withContext(dispatcher) {
            query.clearRecentSearchQueries()
        }
    }
}
