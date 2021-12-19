package com.darekbx.dotpad3.ui.settings

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.darekbx.dotpad3.utils.RequestPermission
import com.darekbx.dotpad3.viewmodel.BackupViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.io.File

@ExperimentalPermissionsApi
@Composable
fun SettingsScreen(
    modifier: Modifier,
    createBackup: () -> Unit,
    restoreBackup: (String?) -> Unit,
    closeBackupStateDialog: () -> Unit,
    backupState: State<BackupViewModel.BackupState?>
) {
    val restoreDialogState = remember { mutableStateOf(false) }
    val backupStateDialogState = remember { mutableStateOf(false) }
    val createBackupState = remember { mutableStateOf(false) }
    val restoreState = remember { mutableStateOf(false) }
    val progressStatus = remember { mutableStateOf(false) }

    val fileIntentResultRequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
        { activityResult -> restoreBackup(activityResult?.rawPath()) }

    if (backupState.value != null) {
        backupStateDialogState.value = true
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        CreateBackupButton(Modifier.fillMaxWidth(), createBackupState)
        RestoreBackupButton(Modifier.fillMaxWidth(), restoreState)

        if (createBackupState.value) {
            createBackupState.value = false
            RequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                createBackup()
            }
        }
        if (restoreState.value) {
            restoreState.value = false
            RequestPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) {
                restoreDialogState.value = true
                progressStatus.value = true
            }
        }
    }

    if (restoreDialogState.value) {
        RestoreDialog(restoreDialogState) {
            fileIntentResultRequest.launch("*/*")
        }
    }

    if (backupStateDialogState.value) {
        progressStatus.value = false
        HandleBackupStates(backupState, backupStateDialogState, closeBackupStateDialog)
    }

    if (progressStatus.value) {
        Box(Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black.copy(0.5F))
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 1.5.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(1.dp)
            )
        }
    }
}

@Composable
private fun HandleBackupStates(
    backupState: State<BackupViewModel.BackupState?>,
    backupStateDialogState: MutableState<Boolean>,
    closeBackupStateDialog: () -> Unit
) {
    when (backupState.value) {
        is BackupViewModel.BackupState.Failed -> {
            val state = backupState.value as BackupViewModel.BackupState.Failed
            BackupStateDialog(state.errorMessage, backupStateDialogState) {
                closeBackupStateDialog()
            }
        }
        is BackupViewModel.BackupState.BackupSuccess -> {
            val state = backupState.value as BackupViewModel.BackupState.BackupSuccess
            BackupStateDialog(
                stringResource(id = R.string.backup_success, state.backupFileName),
                backupStateDialogState
            ) {
                closeBackupStateDialog()
            }
        }
        is BackupViewModel.BackupState.RestoreSuccess -> {
            val state = backupState.value as BackupViewModel.BackupState.RestoreSuccess
            BackupStateDialog(
                stringResource(id = R.string.restore_success, state.dotsCount),
                backupStateDialogState
            ) {
                closeBackupStateDialog()
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
private fun CreateBackupButton(
    modifier: Modifier = Modifier,
    createBackupState: MutableState<Boolean>
) {
    Column(
        modifier.clickable { createBackupState.value = true }
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.backup_create),
            style = Typography.h4,
            color = Color.White
        )
        Text(
            text = stringResource(id = R.string.backup_create_description),
            style = Typography.h5,
            color = Color.Gray
        )
    }
}

@Composable
private fun RestoreBackupButton(
    modifier: Modifier = Modifier,
    restoreState: MutableState<Boolean>
) {
    Column(
        modifier.clickable { restoreState.value = true }
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.backup_restore),
            style = Typography.h4
        )
        Text(
            text = stringResource(id = R.string.backup_restore_description),
            style = Typography.h5,
            color = Color.Gray
        )
    }
}

private fun Uri.rawPath(): String {
    var path = path!!.split(":")[1]
    if (!File(path).exists()) {
        path = "/storage/emulated/0/$path"
    }
    return path
}

@ExperimentalPermissionsApi
@Preview
@Composable
fun PreviewSettingsScreen() {
    val backupState = remember { mutableStateOf(null) }
    SettingsScreen(Modifier.background(Color.Black), { }, { }, { }, backupState)
}
