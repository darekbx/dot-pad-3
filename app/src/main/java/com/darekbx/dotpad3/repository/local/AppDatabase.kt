package com.darekbx.dotpad3.repository.local

import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.darekbx.dotpad3.repository.local.entities.DotDto

@Database(entities = arrayOf(DotDto::class), version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDotsDao(): DotsDao

    companion object {

        fun importFromOldDb(db: SupportSQLiteDatabase) {
            val dotPad2DbPath = "/storage/emulated/0/dotpad_db_20211217_141354.sqlite"
            SQLiteDatabase.openDatabase(dotPad2DbPath, null, SQLiteDatabase.OPEN_READONLY)
                .use { legacyDb ->
                    legacyDb.rawQuery("SELECT * FROM dots", null)
                        .use { c ->
                            c.moveToFirst()
                            do {
                                db.execSQL(
                                    """
INSERT OR IGNORE INTO dots VALUES(
    '${c.getLong(0)}',
    ${DatabaseUtils.sqlEscapeString(c.getString(1))}, 
    '${c.getInt(2)}', 
    '${c.getInt(3)}', 
    '${c.getInt(4)}', 
    '${c.getInt(5)}', 
    '${c.getLong(6)}', 
    '${c.getInt(7)}', 
    '${c.getInt(8)}', 
    '${c.getLong(9)}', 
    '${c.getLong(10)}', 
    '${c.getLong(11)}'
)
"""
                                )
                            } while (c.moveToNext())
                        }
                }
        }
    }
}
