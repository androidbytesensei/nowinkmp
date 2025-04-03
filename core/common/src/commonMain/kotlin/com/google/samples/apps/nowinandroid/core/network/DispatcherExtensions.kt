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

package com.google.samples.apps.nowinandroid.core.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

/**
 * Extension function to get a dispatcher from Koin.
 */
fun KoinComponent.getDispatcher(dispatcher: NiaDispatchers): CoroutineDispatcher {
    return get(dispatcher.asQualifier)
}

/**
 * Extension function to get the application scope from Koin.
 */
fun KoinComponent.getApplicationScope(): CoroutineScope {
    return get(named("ApplicationScope"))
}
