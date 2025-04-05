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

@file:OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)

package com.google.samples.apps.nowinandroid.core.datastore

import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.decodeValueOrNull
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

private const val USER_DATA_KEY = "user_data_key"

class NiaPreferencesDataSource(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher,
) {
    private val _userData = MutableStateFlow(
        settings.decodeValue(
            key = USER_DATA_KEY,
            serializer = UserPreferences.serializer(),
            defaultValue = settings.decodeValueOrNull(
                key = USER_DATA_KEY,
                serializer = UserPreferences.serializer(),
            ) ?: UserPreferences.DEFAULT,
        ),
    )

    val userData = _userData.map(UserPreferences::toUserData)

    suspend fun setFollowedTopicIds(topicIds: Set<String>) = withContext(dispatcher) {
        val preference = settings.getUserPreference()
            .copy(followedTopicIds = topicIds)
            .updateShouldHideOnboardingIfNecessary()
        settings.putUserPreference(preference)
        _userData.value = preference
    }

    suspend fun setTopicIdFollowed(topicId: String, followed: Boolean) = withContext(dispatcher) {
        val preference = settings.getUserPreference()
        val newPreference = preference
            .copy(
                followedTopicIds = if (followed) {
                    preference.followedTopicIds + topicId
                } else {
                    preference.followedTopicIds - topicId
                },
            )
            .updateShouldHideOnboardingIfNecessary()
        settings.putUserPreference(newPreference)
        _userData.value = newPreference
    }

    suspend fun setThemeBrand(themeBrand: ThemeBrand) = withContext(dispatcher) {
        val newPreference = settings.getUserPreference()
            .copy(themeBrand = themeBrand)
        settings.putUserPreference(newPreference)
        _userData.value = newPreference
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) = withContext(dispatcher) {
        val newPreference = settings.getUserPreference()
            .copy(useDynamicColor = useDynamicColor)
        settings.putUserPreference(newPreference)
        _userData.value = newPreference
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) = withContext(dispatcher) {
        val newPreference = settings.getUserPreference()
            .copy(darkThemeConfig = darkThemeConfig)
        settings.putUserPreference(newPreference)
        _userData.value = newPreference
    }

    suspend fun setNewsResourceBookmarked(newsResourceId: String, bookmarked: Boolean) =
        withContext(dispatcher) {
            val preference = settings.getUserPreference()
            val newPreferences = preference
                .copy(
                    bookmarkedNewsResourceIds = if (bookmarked) {
                        preference.bookmarkedNewsResourceIds + newsResourceId
                    } else {
                        preference.bookmarkedNewsResourceIds - newsResourceId
                    },
                )
            settings.putUserPreference(newPreferences)
            _userData.value = newPreferences
        }

    suspend fun setNewsResourceViewed(newsResourceId: String, viewed: Boolean) {
        setNewsResourcesViewed(listOf(newsResourceId), viewed)
    }

    suspend fun setNewsResourcesViewed(newsResourceIds: List<String>, viewed: Boolean) =
        withContext(dispatcher) {
            val preference = settings.getUserPreference()
            val newPreferences = preference
                .copy(
                    viewedNewsResourceIds = if (viewed) {
                        preference.viewedNewsResourceIds + newsResourceIds
                    } else {
                        preference.viewedNewsResourceIds - newsResourceIds.toSet()
                    },
                )
            settings.putUserPreference(newPreferences)
            _userData.value = newPreferences
        }

    suspend fun getChangeListVersions(): ChangeListVersions = withContext(dispatcher) {
        val preferences = settings.getUserPreference()
        return@withContext ChangeListVersions(
            topicVersion = preferences.topicChangeListVersion,
            newsResourceVersion = preferences.newsResourceChangeListVersion,
        )
    }

    /**
     * Update the [ChangeListVersions] using [update].
     */
    suspend fun updateChangeListVersion(update: ChangeListVersions.() -> ChangeListVersions) =
        withContext(dispatcher) {
            val currentPreferences = settings.getUserPreference()
            val updatedChangeListVersions = update(
                ChangeListVersions(
                    topicVersion = currentPreferences.topicChangeListVersion,
                    newsResourceVersion = currentPreferences.newsResourceChangeListVersion,
                ),
            )
            val updatedPreference = currentPreferences.copy(
                topicChangeListVersion = updatedChangeListVersions.topicVersion,
                newsResourceChangeListVersion = updatedChangeListVersions.newsResourceVersion,
            )
            settings.putUserPreference(updatedPreference)
            _userData.value = updatedPreference
        }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) = withContext(dispatcher) {
        val newPreference = settings.getUserPreference()
            .copy(shouldHideOnboarding = shouldHideOnboarding)
        settings.putUserPreference(newPreference)
        _userData.value = newPreference
    }
}

private fun Settings.getUserPreference(): UserPreferences {
    return decodeValue(
        key = USER_DATA_KEY,
        serializer = UserPreferences.serializer(),
        defaultValue = UserPreferences.DEFAULT,
    )
}

private fun Settings.putUserPreference(preference: UserPreferences) {
    encodeValue(
        key = USER_DATA_KEY,
        serializer = UserPreferences.serializer(),
        value = preference,
    )
}

private fun UserPreferences.updateShouldHideOnboardingIfNecessary(): UserPreferences {
    return if (followedTopicIds.isEmpty() && followedAuthorIds.isEmpty()) {
        this.copy(shouldHideOnboarding = false)
    } else {
        this
    }
}