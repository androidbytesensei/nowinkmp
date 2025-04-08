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

package com.google.samples.apps.nowinandroid.core.ui.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData

/**
 * Extension function to add drag and drop source capability to any composable
 * Works across KMP targets using the official Compose API
 */
fun Modifier.dragSource(
    content: String,
    label: String,
    onDragStart: (() -> Unit)? = null,
    onDragEnd: (() -> Unit)? = null
): Modifier = this.then(dragSourceImpl(content, label, onDragStart, onDragEnd))

/**
 * Platform-specific implementation of drag source modifier using official Compose API
 */
expect fun Modifier.dragSourceImpl(
    content: String,
    label: String,
    onDragStart: (() -> Unit)? = null,
    onDragEnd: (() -> Unit)? = null
): Modifier

/**
 * Creates a drag and drop transfer data from content
 */
expect fun createDragData(content: String, label: String): DragAndDropTransferData