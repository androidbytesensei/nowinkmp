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

package com.google.samples.apps.nowinandroid.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Default dialog with confirm and dismiss button.
 *
 * @param dialogContent arguments to compose the dialog
 * @param isDialogOpen flag to indicate if the dialog should be open
 * @param onDismissRequest function to be called user requests to dismiss the dialog
 * @param modifier the modifier to be applied to the dialog
 */
@Composable
fun LibraryDialog(
    dialogContent: DialogContent,
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = dialogContent.title) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = dialogContent.text)
                }
            },
            confirmButton = {
                Button(onClick = dialogContent.onConfirmAction) {
                    Text(text = dialogContent.confirmText)
                }
            },
            dismissButton = {
                dialogContent.dismissText?.let {
                    OutlinedButton(onClick = onDismissRequest) {
                        Text(text = dialogContent.dismissText)
                    }
                }
            },
            modifier = modifier,
        )
    }
}

/**
 * Arguments to be used with [LibraryDialog].
 *
 * @property title the dialog title
 * @property text the dialog content text
 * @property confirmText the text to be used in the confirm button
 * @property dismissText the text to be used in the dismiss button
 * @property onConfirmAction the action to be executed when the user confirms the dialog
 */
data class DialogContent(
    val title: String,
    val text: String,
    val confirmText: String,
    val dismissText: String? = null,
    val onConfirmAction: () -> Unit,
)

@Preview
@Composable
private fun DialogPreview() {
    NiaTheme {
        val arguments = DialogContent(
            title = "Something regrettable",
            text = "Are you sure that do you want to do something regrettable?",
            confirmText = "Regret",
            dismissText = "Cancel",
            onConfirmAction = {},
        )

        LibraryDialog(
            dialogContent = arguments,
            isDialogOpen = true,
            onDismissRequest = {},
        )
    }
}