package com.darekbx.dotpad3.di

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.darekbx.dotpad3.repository.local.AppDatabase
import com.darekbx.dotpad3.repository.local.AppDatabase.Companion.DB_FILE_PATH
import com.darekbx.dotpad3.viewmodel.DotsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File
import java.io.FileOutputStream

val appModule = module {

    single { get<AppDatabase>().getDotsDao() }

    single {
       //
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, "dotpad3")
           /* .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val version2db = SQLiteDatabase.openDatabase(DB_FILE_PATH,null,SQLiteDatabase.OPEN_READONLY)
                    val c = version2db.rawQuery("SELECT * FROM dots",null)
                    c.moveToFirst()
                    do  {
                        db.execSQL("INSERT OR IGNORE INTO dots VALUES('${c.getLong(0)}', ${DatabaseUtils.sqlEscapeString(c.getString(1))}, '${c.getInt(2)}', '${c.getInt(3)}', '${c.getInt(4)}', '${c.getInt(5)}', '${c.getLong(6)}', '${c.getInt(7)}', '${c.getInt(8)}', '${c.getLong(9)}', '${c.getLong(10)}', '${c.getLong(11)}')")
                    } while (c.moveToNext())
                    c.close()
                    version2db.close()
                }
            })*/
            .build() }

    viewModel { DotsViewModel(get()) }

}
