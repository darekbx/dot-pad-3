package com.darekbx.dotpad3.viewmodel

sealed class BackupState {
    class BackupSuccess(val backupFileName: String) : BackupState()
    class RestoreSuccess(val dotsCount: Int) : BackupState()
    class Failed(val errorMessage: String) : BackupState()
}
