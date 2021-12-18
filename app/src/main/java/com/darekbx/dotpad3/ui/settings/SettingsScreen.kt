package com.darekbx.dotpad3.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.darekbx.dotpad3.R
import com.darekbx.dotpad3.ui.dots.toColor
import com.darekbx.dotpad3.ui.theme.Typography
import com.darekbx.dotpad3.ui.theme.dialogBackgroud
import com.darekbx.dotpad3.ui.theme.dotRed
import com.darekbx.dotpad3.viewmodel.BackupState

@Composable
fun SettingsScreen(
    modifier: Modifier,
    createBackup: () -> Unit,
    restoreBackup: (String) -> Unit,
    closeBackupStateDialog: () -> Unit,
    backupState: State<BackupState?>
) {
    val restoreDialogState = remember { mutableStateOf(false) }
    val backupStateDialogState = remember { mutableStateOf(false) }

    if (backupState.value != null) {
        backupStateDialogState.value = true
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        CreateBackupButton { createBackup() }
        RestoreBackupButton { restoreDialogState.value = true }
    }

    if (restoreDialogState.value) {
        RestoreDialog(restoreDialogState) {
            // TODO
            restoreBackup("file_url")
        }
    }

    if (backupStateDialogState.value) {
        when (backupState.value) {
            is BackupState.Failed -> {
                val state = backupState.value as BackupState.Failed
                BackupStateDialog(state.errorMessage, backupStateDialogState) {
                    closeBackupStateDialog()
                }
            }
            is BackupState.BackupSuccess -> {
                val state = backupState.value as BackupState.BackupSuccess
                BackupStateDialog(
                    stringResource(id = R.string.backup_success, state.backupFileName),
                    backupStateDialogState
                ) {
                    closeBackupStateDialog()
                }
            }
            is BackupState.RestoreSuccess -> {
                val state = backupState.value as BackupState.RestoreSuccess
                BackupStateDialog(
                    stringResource(id = R.string.restore_success, state.dotsCount),
                    backupStateDialogState
                ) {
                    closeBackupStateDialog()
                }
            }
        }
    }
}

@Composable
private fun RestoreDialog(
    restoreDialogState: MutableState<Boolean>,
    restoreBackup: () -> Unit
) {
    AlertDialog(
        backgroundColor = dialogBackgroud,
        onDismissRequest = { restoreDialogState.value = false },
        confirmButton = {
            Button(onClick = {
                restoreBackup()
                restoreDialogState.value = false
            }) {
                Text(
                    text = stringResource(id = R.string.button_ok),
                    color = dotRed.toColor()
                )
            }
        },
        dismissButton = {
            Button(onClick = {
                restoreDialogState.value = false
            }) {
                Text(text = stringResource(id = R.string.button_cancel))
            }
        },
        text = {
            Text(text = stringResource(id = R.string.restore_dialog))
        }
    )
}

@Composable
private fun BackupStateDialog(
    message: String,
    backupStateDialogState: MutableState<Boolean>,
    onClose: () -> Unit
) {
    AlertDialog(
        backgroundColor = dialogBackgroud,
        onDismissRequest = {
            backupStateDialogState.value = false
            onClose()
        },
        confirmButton = {
            Button(onClick = {
                backupStateDialogState.value = false
                onClose()
            }) {
                Text(text = stringResource(id = R.string.button_ok))
            }
        },
        text = {
            Text(text = message)
        }
    )
}

@Composable
private fun CreateBackupButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier.clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.backup_create),
            style = Typography.h5
        )
        Text(
            text = stringResource(id = R.string.backup_create_description),
            style = Typography.h6,
            color = Color.Gray
        )
    }
}

@Composable
private fun RestoreBackupButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier.clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.backup_restore),
            style = Typography.h5
        )
        Text(
            text = stringResource(id = R.string.backup_restore_description),
            style = Typography.h6,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    val backupState = remember { mutableStateOf(null) }
    SettingsScreen(Modifier.background(Color.Black), { }, { }, { }, backupState)
}