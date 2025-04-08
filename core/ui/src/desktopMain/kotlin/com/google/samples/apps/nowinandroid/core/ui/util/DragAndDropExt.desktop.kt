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

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import java.awt.Component
import java.awt.datatransfer.StringSelection
import java.awt.dnd.DnDConstants
import java.awt.dnd.DragGestureListener
import java.awt.dnd.DragSource

/**
 * Platform-specific implementation of drag source modifier using official Compose API
 */
@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.dragSourceImpl(
    content: String,
    label: String,
    onDragStart: (() -> Unit)?,
    onDragEnd: (() -> Unit)?,
): Modifier = composed {
    // Access desktop component within the composed scope
    val component = getAwtComponentFromComposeContext()
    val transferable = StringSelection(content)

    this.pointerInput(content, label) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()

                if (event.type == PointerEventType.Press) {
                    // Start drag operation
                    onDragStart?.invoke()

                    // Create a drag source
                    val dragSource = DragSource()

                    // Create a drag gesture recognizer with a listener that will be called immediately
                    val listener = DragGestureListener { dge -> // This is called when the drag gesture is recognized
                        dge.startDrag(null, transferable)
                    }

                    // The proper way to initiate a drag in AWT/Swing - create a recognizer
                    // and immediately get it to recognize a drag at the current location
                    val recognizer = dragSource.createDefaultDragGestureRecognizer(
                        component,
                        DnDConstants.ACTION_COPY,
                        listener
                    )

                    // The position where the drag should start
                    val position = event.changes.first().position

                    // Programmatically trigger the drag - this requires access to internal APIs
                    // which isn't ideal, so in a real implementation you might:
                    // 1. Use reflection to access internal methods
                    // 2. Use JNA/JNI to trigger OS-level drag events
                    // 3. Use a custom bridge between Compose and AWT

                    // For demonstration purposes (not working code):
                     simulateDragGesture(component, position.x.toInt(), position.y.toInt())

                    // Wait for drag to complete
                    awaitPointerEvent(PointerEventPass.Final)
                    onDragEnd?.invoke()
                }
            }
        }
    }
}

/**
 * This would be an implementation that bridges between Compose and AWT
 * to programmatically trigger drag gestures
 */
private fun simulateDragGesture(component: Component, x: Int, y: Int) {
    // In a real implementation, this would:
    // 1. Create the necessary mouse events
    // 2. Dispatch them to the component
    // 3. Communicate with the drag system

    // This is complex and requires platform-specific code or reflection
}

/**
 * Helper function to get AWT Component from Compose context
 * This is a placeholder - actual implementation would depend on Compose Desktop integration
 */
private fun getAwtComponentFromComposeContext(): Component {
    // In a real implementation, this would get the actual AWT Component
    return java.awt.Canvas() // Placeholder
}

/**
 * Creates a drag and drop transfer data from content
 */
@OptIn(ExperimentalComposeUiApi::class)
actual fun createDragData(
    content: String,
    label: String,
): DragAndDropTransferData {
    return DragAndDropTransferData(
        transferable = DragAndDropTransferable(StringSelection(content)),
        supportedActions = listOf(
            DragAndDropTransferAction.Copy,
            DragAndDropTransferAction.Link,
            DragAndDropTransferAction.Move,
        )
    )
}