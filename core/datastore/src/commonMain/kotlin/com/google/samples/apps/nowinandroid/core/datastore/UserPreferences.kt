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

package com.google.samples.apps.nowinandroid.core.datastore

import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand
import com.google.samples.apps.nowinandroid.core.model.data.UserData
import kotlinx.serialization.Serializable

/**
 * Data class representing user preferences
 */
@Serializable
data class UserPreferences(
    val topicChangeListVersion: Int,
    val authorChangeListVersion: Int,
    val newsResourceChangeListVersion: Int,
    val hasDoneIntToStringIdMigration: Boolean,
    val hasDoneListToMapMigration: Boolean,
    val followedTopicIds: Set<String>,
    val followedAuthorIds: Set<String>,
    val bookmarkedNewsResourceIds: Set<String>,
    val viewedNewsResourceIds: Set<String>,
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
    val shouldHideOnboarding: Boolean,
    val useDynamicColor: Boolean,
){
    companion object {
        val DEFAULT = UserPreferences(
            topicChangeListVersion = 0,
            authorChangeListVersion = 0,
            newsResourceChangeListVersion = 0,
            hasDoneIntToStringIdMigration = false,
            hasDoneListToMapMigration = false,
            themeBrand = ThemeBrand.DEFAULT,
            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
            shouldHideOnboarding = false,
            useDynamicColor = false,
            followedTopicIds = emptySet(),
            followedAuthorIds = emptySet(),
            bookmarkedNewsResourceIds = emptySet(),
            viewedNewsResourceIds = emptySet(),
        )
    }
}

/**
 * Extension function to convert to UserData
 */
fun UserPreferences.toUserData(): UserData {
    return UserData(
        bookmarkedNewsResources = bookmarkedNewsResourceIds,
        viewedNewsResources = viewedNewsResourceIds,
        followedTopics = followedTopicIds,
        themeBrand = themeBrand,
        darkThemeConfig = darkThemeConfig,
        useDynamicColor = useDynamicColor,
        shouldHideOnboarding = shouldHideOnboarding,
    )
}