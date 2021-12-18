package com.darekbx.dotpad3.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.darekbx.dotpad3.BuildConfig
import com.darekbx.dotpad3.reminder.ReminderCreator
import com.darekbx.dotpad3.repository.local.AppDatabase
import com.darekbx.dotpad3.viewmodel.BackupViewModel
import com.darekbx.dotpad3.viewmodel.DotsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { get<AppDatabase>().getDotsDao() }

    single {
        //
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, "dotpad3")
             .addCallback(object: RoomDatabase.Callback() {
                 override fun onCreate(db: SupportSQLiteDatabase) {
                     AppDatabase.importFromOldDb(db)
                 }
             })
            .build()
    }

    factory { ReminderCreator(androidContext().contentResolver, BuildConfig.USER_EMAIL) }

    viewModel { DotsViewModel(get(), get()) }
    viewModel { BackupViewModel(get()) }

}
