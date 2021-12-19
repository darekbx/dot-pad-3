package com.darekbx.dotpad3.di

import androidx.room.Room
import com.darekbx.dotpad3.BuildConfig
import com.darekbx.dotpad3.reminder.ReminderCreator
import com.darekbx.dotpad3.repository.local.AppDatabase
import com.darekbx.dotpad3.repository.local.AppDatabase.Companion.DB_NAME
import com.darekbx.dotpad3.viewmodel.BackupViewModel
import com.darekbx.dotpad3.viewmodel.DotsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single { get<AppDatabase>().getDotsDao() }
    single(named("dbName")) { androidContext().getDatabasePath(DB_NAME) }

    single {
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, DB_NAME)
            .build()
    }

    factory { ReminderCreator(androidContext().contentResolver, BuildConfig.USER_EMAIL) }

    viewModel { DotsViewModel(get(), get()) }
    viewModel { BackupViewModel(get(qualifier = named("dbName")), get()) }
}
