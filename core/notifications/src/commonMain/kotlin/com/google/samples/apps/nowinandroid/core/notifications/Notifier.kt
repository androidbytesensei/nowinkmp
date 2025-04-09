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

package com.google.samples.apps.nowinandroid.core.notifications

import com.google.samples.apps.nowinandroid.core.model.data.NewsResource

const val MAX_NUM_NOTIFICATIONS = 5
internal const val TARGET_ACTIVITY_NAME = "com.google.samples.apps.nowinandroid.MainActivity"
internal const val NEWS_NOTIFICATION_REQUEST_CODE = 0
internal const val NEWS_NOTIFICATION_SUMMARY_ID = 1
internal const val NEWS_NOTIFICATION_CHANNEL_ID = ""
internal const val NEWS_NOTIFICATION_GROUP = "NEWS_NOTIFICATIONS"
internal const val DEEP_LINK_SCHEME_AND_HOST = "https://www.nowinandroid.apps.samples.google.com"
internal const val DEEP_LINK_FOR_YOU_PATH = "foryou"
internal const val DEEP_LINK_BASE_PATH = "$DEEP_LINK_SCHEME_AND_HOST/$DEEP_LINK_FOR_YOU_PATH"
const val DEEP_LINK_NEWS_RESOURCE_ID_KEY = "linkedNewsResourceId"
const val DEEP_LINK_URI_PATTERN = "$DEEP_LINK_BASE_PATH/{$DEEP_LINK_NEWS_RESOURCE_ID_KEY}"

/**
 * Interface for creating notifications in the app
 */
interface Notifier {
    fun postNewsNotifications(newsResources: List<NewsResource>)
}
