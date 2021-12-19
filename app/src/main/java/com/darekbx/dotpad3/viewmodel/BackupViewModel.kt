package com.darekbx.dotpad3.viewmodel

import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darekbx.dotpad3.repository.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BackupViewModel(
    private val databaseFile: File,
    private val appDatabase: AppDatabase,
) : ViewModel() {

    sealed class BackupState {
        class BackupSuccess(val backupFileName: String) : BackupState()
        class RestoreSuccess(val dotsCount: Int) : BackupState()
        class Failed(val errorMessage: String) : BackupState()
    }

    var backupState = mutableStateOf<BackupState?>(null)

    fun createBackup() {
        runInIO {
            try {
                val currentDate = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(Calendar.getInstance().timeInMillis)
                val outFile = File("/storage/emulated/0/Download/", "dotpad_3_$currentDate.sqlite")
                CoroutineScope(Dispatchers.IO).launch {
                    databaseFile.copyTo(outFile)
                    backupState.value = BackupState.BackupSuccess(outFile.name)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                backupState.value = BackupState.Failed(e.message ?: "Unknown error")
            }
        }
    }

    fun restoreBackup(sourceFileUrl: String?) {
        if (sourceFileUrl == null) {
            backupState.value = BackupState.Failed("Source file path is null!")
            return
        }
        runInIO {
            try {
                var rowsInserted = 0
                SQLiteDatabase.openDatabase(sourceFileUrl, null, SQLiteDatabase.OPEN_READWRITE)
                    .use { legacyDb ->
                        appDatabase.openHelper.writableDatabase.query("DELETE FROM dots", null)
                        legacyDb.rawQuery("SELECT * FROM dots", null)
                            .use { c ->
                                c.moveToFirst()
                                do {
                                    val values = contentValuesOf(
                                        "id" to c.getLong(0),
                                        "text" to c.getString(1),
                                        "size" to c.getInt(2),
                                        "color" to c.getInt(3),
                                        "position_x" to c.getInt(4),
                                        "position_y" to c.getInt(5),
                                        "created_date" to c.getLong(6),
                                        "is_archived" to c.getInt(7),
                                        "is_sticked" to c.getInt(8),
                                        "reminder" to c.getLong(9),
                                        "calendar_event_id" to c.getLong(10),
                                        "calendar_reminder_id" to c.getLong(11)
                                    )
                                    appDatabase.openHelper.writableDatabase
                                        .insert("dots", SQLiteDatabase.CONFLICT_IGNORE, values)

                                    rowsInserted++
                                } while (c.moveToNext())
                            }
                    }
                backupState.value = BackupState.RestoreSuccess(rowsInserted)
            } catch (e: Exception) {
                e.printStackTrace()
                backupState.value = BackupState.Failed(e.message ?: "Unknown error")
            }
        }
    }

    fun resetBackupState() {
        backupState.value = null
    }

    private fun runInIO(block: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                block()
            }
        }
    }
}
