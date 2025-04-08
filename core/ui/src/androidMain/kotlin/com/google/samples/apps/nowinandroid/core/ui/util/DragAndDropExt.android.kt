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

import android.content.ClipData
import android.os.Build
import android.view.View
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView

/**
 * Platform-specific implementation of drag source modifier using official Compose API
 */
actual fun Modifier.dragSourceImpl(
    content: String,
    label: String,
    onDragStart: (() -> Unit)?,
    onDragEnd: (() -> Unit)?,
): Modifier = composed {
    val view = LocalView.current
    val clipData = remember(content, label) {
        ClipData.newPlainText(label, content)
    }

    val dragFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        View.DRAG_FLAG_GLOBAL
    } else {
        0
    }

    this.pointerInput(content, label) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()

                if (event.type == PointerEventType.Press) {
                    // Start drag operation
                    onDragStart?.invoke()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        view.startDragAndDrop(
                            clipData,
                            View.DragShadowBuilder(view),
                            null,
                            dragFlags
                        )
                    } else {
                        view.startDrag(
                            clipData,
                            View.DragShadowBuilder(view),
                            null,
                            dragFlags
                        )
                    }

                    // Wait for drag to complete
                    awaitPointerEvent(PointerEventPass.Final)
                    onDragEnd?.invoke()
                }
            }
        }
    }
}

/**
 * Creates a drag and drop transfer data from content
 */
actual fun createDragData(
    content: String,
    label: String,
): DragAndDropTransferData {
    return DragAndDropTransferData(ClipData.newPlainText(label, content))
}