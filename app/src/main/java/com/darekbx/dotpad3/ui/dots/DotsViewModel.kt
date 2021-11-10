package com.darekbx.dotpad3.ui.dots

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class DotsViewModel : ViewModel() {

    var dots = mutableStateListOf<Dot>().apply {
        add(Dot("142d", 100F, 100F, DotSize.SMALL, Color(0xFFEF5350)))
        add(
            Dot(
                "600s",
                200F,
                200F,
                DotSize.MEDIUM,
                Color(0xFF26a69a),
                createdDate = 1636109037074L,
                reminder = 1636109037074L + 51 * 60 * 1000
            )
        )
        add(Dot("Three", 400F, 400F, DotSize.LARGE, Color(0xFF42a5f5)))
        add(Dot("Three", 600F, 600F, DotSize.HUGE, Color(0xFF42a5f5)))
    }
        private set

    fun addItem(item: Dot) {
        dots.add(item)
    }

    fun removeItem(item: Dot) {
        dots.remove(item)
    }
}