package com.darekbx.dotpad3.ui.dots

import androidx.compose.ui.graphics.Color
import android.graphics.Color as GColor
import java.io.Serializable

data class Dot(
    var id: Long? = null,
    var text: String,
    var x: Float,
    var y: Float,
    var size: DotSize? = null,
    var color: DotColor? = null,
    var createdDate: Long = System.currentTimeMillis(),
    var isArchived: Boolean = false,
    var isSticked: Boolean = false,
    var reminder: Long? = null,
    var calendarEventId: Long? = null,
    var calendarReminderId: Long? = null
) {

    val isNew = id == null

    fun hasReminder(): Boolean {
        return (reminder ?: 0L) > 0L
    }

    fun requireSize(): DotSize = size ?: throw IllegalStateException("Size is null")

    fun requireColor(): DotColor = color ?: throw IllegalStateException("Color is null")
}

enum class DotSize(val size: Int) {
    SMALL(5),
    MEDIUM(6),
    LARGE(8),
    HUGE(10)
}

class DotColor(val r: Float, val g: Float, val b: Float) : Serializable {

    fun equalsColor(other: DotColor?): Boolean {
        if (other == null) return false
        return other.r == r && other.g == g && other.b == b
    }
}

fun Color.toDotColor(): DotColor = DotColor(red, green, blue)
fun DotColor.toColor(): Color = Color(r, g, b)
fun DotColor.toIntColor(): Int = GColor.argb(1F, r, g, b)
