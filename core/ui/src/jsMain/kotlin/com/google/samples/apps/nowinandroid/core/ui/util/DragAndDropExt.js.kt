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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import org.w3c.dom.Element
import org.w3c.dom.events.Event

/**
 * Platform-specific implementation of drag source modifier using official Compose API
 */
actual fun Modifier.dragSourceImpl(
    content: String,
    label: String,
    onDragStart: (() -> Unit)?,
    onDragEnd: (() -> Unit)?,
): Modifier = this.then(
    Modifier.pointerInput(content, label) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()

                if (event.type == PointerEventType.Press) {
                    // Get the DOM element
                    val element = getDomElementFromComposeContext()

                    // Make element draggable
                    element.setAttribute("draggable", "true")

                    // Attach event listeners
                    val dragStartListener: (Event) -> Unit = { domEvent: Event ->
                        val dragEvent = domEvent.unsafeCast<org.w3c.dom.DragEvent>()
                        val dataTransfer = dragEvent.dataTransfer

                        dataTransfer?.setData("text/plain", content)
                        dataTransfer?.setData("text/label", label)

                        onDragStart?.invoke()
                    }

                    val dragEndListener: (Event) -> Unit = { _: Event ->
                        onDragEnd?.invoke()
                    }

                    // Add listeners
                    element.addEventListener("dragstart", dragStartListener)
                    element.addEventListener("dragend", dragEndListener)

                    // Wait for next press event (effectively reset for next drag)
                    awaitPointerEvent(PointerEventPass.Final)

                    // Clean up
                    element.removeEventListener("dragstart", dragStartListener)
                    element.removeEventListener("dragend", dragEndListener)
                }
            }
        }
    },
)

/**
 * Creates a drag and drop transfer data from content
 */
actual fun createDragData(
    content: String,
    label: String,
): DragAndDropTransferData {
    return DragAndDropTransferData()
}

/**
 * Helper function to get DOM element from Compose context
 * This is a placeholder - actual implementation would depend on Compose Web integration
 */
private fun getDomElementFromComposeContext(): Element {
    // In a real implementation, this would get the actual DOM Element
    return js("document.createElement('div')").unsafeCast<Element>()
}