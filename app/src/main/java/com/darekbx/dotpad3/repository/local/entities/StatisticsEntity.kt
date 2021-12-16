package com.darekbx.dotpad3.repository.local.entities

import androidx.room.ColumnInfo

class StatisticsEntity(
    @ColumnInfo(name = "occurrences") var occurrences: Int?,
    @ColumnInfo(name = "value") var value: Int?
)