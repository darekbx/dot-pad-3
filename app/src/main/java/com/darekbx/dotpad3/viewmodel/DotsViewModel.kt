package com.darekbx.dotpad3.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import com.darekbx.dotpad3.repository.local.DotsDao
import com.darekbx.dotpad3.ui.dots.Dot
import com.darekbx.dotpad3.ui.dots.DotColor
import com.darekbx.dotpad3.ui.dots.DotSize
import com.darekbx.dotpad3.ui.theme.dotBlue
import com.darekbx.dotpad3.ui.theme.dotRed
import com.darekbx.dotpad3.ui.theme.dotTeal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DotsViewModel(
    private val dao: DotsDao
) : ViewModel() {

    var selectedDot = mutableStateOf<Dot?>(null)
    var dialogState = mutableStateOf(false)
    var datePickerState = mutableStateOf(false)
    var timePickerState = mutableStateOf(false)

    private var y: Int = 0
    private var m: Int = 0
    private var d: Int = 0

    fun allDots(): LiveData<List<Dot>> {
        val liveData = MutableLiveData<List<Dot>>()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val all = dao.fetchActiveSync()
                    .map { dto ->
                        with(dto) {
                            val c = Color(color)
                            Dot(
                                id,
                                text,
                                positionX.toFloat(),
                                positionY.toFloat(),
                                DotSize.values().find { it.size == size },
                                DotColor(c.red, c.green, c.blue),
                                createdDate,
                                isArchived,
                                isSticked,
                                reminder?.takeIf { it > 0 },
                                calendarEventId,
                                calendarReminderId
                            )
                        }
                    }

                liveData.postValue(all)
            }
        }

        return liveData
    }

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
        add(Dot(4L, "Three", 600F, 600F, DotSize.HUGE, dotBlue, isSticked = true))
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

    fun showDatePicker() {
        datePickerState.value = true
    }

    fun saveDate(year: Int, month: Int, day: Int) {
        y = year
        m = month
        d = day
    }

    fun saveTime(hour: Int, minute: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, y)
        c.set(Calendar.MONTH, m)
        c.set(Calendar.DAY_OF_MONTH, d)
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, minute)
        selectedDot?.value?.reminder = c.timeInMillis
    }

    fun showTimePicker() {
        datePickerState.value = false
        timePickerState.value = true
    }

    fun dismissPickers() {
        datePickerState.value = false
        timePickerState.value = false
    }

    fun selectDot(dot: Dot) {
        dialogState.value = true
        selectedDot.value = dot
    }

    fun discardChanges() {
        dialogState.value = false
        selectedDot.value = null
    }
}
