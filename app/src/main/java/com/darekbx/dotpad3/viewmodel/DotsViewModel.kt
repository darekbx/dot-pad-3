package com.darekbx.dotpad3.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.darekbx.dotpad3.ui.dots.Dot
import com.darekbx.dotpad3.ui.dots.DotSize
import com.darekbx.dotpad3.ui.theme.dotBlue
import com.darekbx.dotpad3.ui.theme.dotRed
import com.darekbx.dotpad3.ui.theme.dotTeal

class DotsViewModel : ViewModel() {

    var selectedDot = mutableStateOf<Dot?>(null)
    var dialogState = mutableStateOf(false)

    var dots = mutableStateListOf<Dot>().apply {
        add(Dot(1L, "142d", 100F, 100F, DotSize.SMALL, dotRed))
        add(
            Dot(
                2L,
                "600s",
                200F,
                200F,
                DotSize.MEDIUM,
                dotTeal,
                createdDate = 1636109037074L,
                reminder = 1636109037074L + 51 * 60 * 1000
            )
        )
        add(Dot(3L, "Three", 400F, 400F, DotSize.LARGE, dotBlue))
        add(Dot(4L, "Three", 600F, 600F, DotSize.HUGE, dotBlue))
    }
        private set

    fun saveItem(item: Dot) {
        dialogState.value = false
        // Check if dot id is null then save, when not null update
        if (item.id == null) {
            dots.add(item)
        } else {
            dots.removeIf { it.id == item.id }
            dots.add(item)
        }
    }

    fun removeItem(item: Dot) {
        dialogState.value = false
        dots.remove(item)
    }

    fun resetTime(item: Dot) {
        dialogState.value = false
    }

    fun addReminder(item: Dot) {
    }

    fun selectDot(dot: Dot) {
        dialogState.value = true
        selectedDot.value = dot
    }
}
