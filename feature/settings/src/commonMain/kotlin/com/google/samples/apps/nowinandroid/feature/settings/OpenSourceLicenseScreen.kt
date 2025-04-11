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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTextButton
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.m3.rememberLibraries
import com.mikepenz.aboutlibraries.ui.compose.util.author
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import settings.generated.resources.Res
import settings.generated.resources.feature_settings_dismiss_dialog_button_text
import settings.generated.resources.feature_settings_licenses

@Composable
fun OpenSourceLicenseScreen(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.fillMaxSize(),
        title = {
            Text(
                text = stringResource(Res.string.feature_settings_licenses),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            OpenSourceContent()
        },
        confirmButton = {
            NiaTextButton(
                onClick = onDismissRequest,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Text(
                    text = stringResource(Res.string.feature_settings_dismiss_dialog_button_text),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun OpenSourceContent(
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedLibrary by remember { mutableStateOf<Library?>(null) }

    val libraries by rememberLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }

    libraries?.let {
        Box(modifier = modifier) {
            LazyColumn {
                items(items = it.libraries) { library ->
                    OpenSourceItem(
                        library = library,
                        onClick = {
                            selectedLibrary = library
                            showDialog = true
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }
        }

        if (selectedLibrary != null && selectedLibrary.isLicenseAvailable()) {
            LibraryDialog(
                dialogContent = DialogContent(
                    title = selectedLibrary?.name ?: "",
                    text = selectedLibrary?.licenses?.firstOrNull()?.licenseContent ?: "",
                    confirmText = stringResource(Res.string.feature_settings_dismiss_dialog_button_text),
                    onConfirmAction = { showDialog = false },
                ),
                isDialogOpen = showDialog,
                onDismissRequest = { showDialog = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(vertical = 16.dp),
            )
        }
    }
}

@Composable
private fun OpenSourceItem(
    library: Library,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = library.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${library.artifactVersion}",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Text(
            text = library.author,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
        )
        SuggestionChip(
            label = {
                Text(
                    text = library.licenses.firstOrNull()?.name ?: "",
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            shape = MaterialTheme.shapes.extraLarge,
            colors = SuggestionChipDefaults
                .suggestionChipColors()
                .copy(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            onClick = { onClick() },
        )
    }
}

private fun Library?.isLicenseAvailable(): Boolean =
    this
        ?.licenses
        ?.firstOrNull()
        ?.licenseContent
        ?.isBlank() == false