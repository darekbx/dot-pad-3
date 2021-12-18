package com.darekbx.dotpad3.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.darekbx.dotpad3.repository.local.DotsDao

class BackupViewModel(
    private val dao: DotsDao,
) : ViewModel() {

    var backupState = mutableStateOf<BackupState?>(null)

    fun createBackup() {
        //backupState.value = BackupState.Failed("Create error")
        backupState.value = BackupState.BackupSuccess("dots_3_12_12_2021_14_35.sqlite")
    }

    fun restoreBackup(sourceFileUrl: String) {
        //backupState.value = BackupState.Failed("Restore error")
        backupState.value = BackupState.RestoreSuccess(4324)
    }

    fun resetBackupState() {
        backupState.value = null
    }
}
