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

import com.google.samples.apps.nowinandroid.core.database.NewsResources
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import kotlinx.datetime.Instant

class NewsResourceMapper : EntityMapper<NewsResources, NewsResourceEntity> {
    override fun mapToDomain(entity: NewsResources): NewsResourceEntity {
        return NewsResourceEntity(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            url = entity.url,
            headerImageUrl = entity.header_image_url,
            publishDate = Instant.fromEpochMilliseconds(entity.publish_date),
            type = entity.type,
        )
    }

    override fun mapToEntity(domain: NewsResourceEntity): NewsResources {
        return NewsResources(
            id = domain.id,
            title = domain.title,
            content = domain.content,
            url = domain.url,
            header_image_url = domain.headerImageUrl,
            publish_date = domain.publishDate.toEpochMilliseconds(),
            type = domain.type,
        )
    }
}
