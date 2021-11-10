package com.darekbx.dotpad3.ui.dots

import androidx.compose.ui.graphics.Color

data class Dot(
    var text: String,
    var x: Float,
    var y: Float,
    val size: DotSize,
    val color: Color,
    var createdDate: Long = System.currentTimeMillis(),
    var isArchived: Boolean = false,
    var isSticked: Boolean = false,
    val reminder: Long? = null,
    var calendarEventId: Long? = null,
    var calendarReminderId: Long? = null
)

enum class DotSize {
    SMALL,
    MEDIUM,
    LARGE,
    HUGE
}