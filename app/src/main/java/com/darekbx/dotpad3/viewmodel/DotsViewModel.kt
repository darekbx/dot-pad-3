package com.darekbx.dotpad3.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.darekbx.dotpad3.model.StatisticValue
import com.darekbx.dotpad3.reminder.ReminderCreator
import com.darekbx.dotpad3.repository.local.DotsDao
import com.darekbx.dotpad3.repository.local.entities.StatisticsEntity
import com.darekbx.dotpad3.ui.dots.Dot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DotsViewModel(
    private val dao: DotsDao,
    private val reminderCreator: ReminderCreator
) : ViewModel() {

    var selectedDot = mutableStateOf<Dot?>(null)
    var dialogState = mutableStateOf(false)
    var datePickerState = mutableStateOf(false)
    var timePickerState = mutableStateOf(false)
    var deleteReminderState = mutableStateOf(false)

    var activeDotsCount = 0

    private var reminderYear: Int = 0
    private var reminderMonth: Int = 0
    private var reminderDay: Int = 0
    private var reminderChanged = false

    fun activeDots(): LiveData<List<Dot>> =
        Transformations.map(dao.fetchActive()) { dots ->
            activeDotsCount = dots.size
            dots.map { dto -> dto.toDot() }
        }

    fun archivedDots(): LiveData<List<Dot>> =
        Transformations.map(dao.fetchArchive(Int.MAX_VALUE, 0)) { dots ->
            dots.map { dto -> dto.toDot() }
        }

    fun saveItem(dot: Dot) {
        dialogState.value = false
        runInIO {
            addReminder(dot)
            if (dot.id == null) {
                dao.add(dot.toDotDto())
            } else {
                dao.update(dot.toDotDto())
            }
        }
    }

    fun restore(dot: Dot) {
        runInIO {
            val dto = dot.toDotDto()
            dto.isArchived = false
            dao.update(dto)
        }
    }

    fun delete(dot: Dot) {
        runInIO {
            dao.deleteDot(dot.id!!)
        }
    }

    fun countAll(): LiveData<Int> = dao.countStatistics()

    fun colorStatistics() = mapToPercents(dao.colorStatistics())

    fun sizeStatistics() = mapToPercents(dao.sizeStatistics())

    private fun mapToPercents(values: LiveData<List<StatisticsEntity>>):
            LiveData<List<StatisticValue>> =
        Transformations.map(values, { statisticValues ->
            val sum = statisticValues.sumOf { it.occurrences ?: 0 }.toFloat()
            return@map statisticValues.map { statisticValue ->
                val percent = (statisticValue.occurrences ?: 0) / sum * 100F
                return@map StatisticValue(percent, statisticValue.value!!)
            }
        })

    private fun addReminder(dot: Dot) {
        android.util.Log.v("----------------", "addReminder: $reminderChanged")
        if (reminderChanged && dot.hasReminder()) {
            val (eventId, reminderId) = reminderCreator.addReminder(dot)
            dot.calendarEventId = eventId
            dot.calendarReminderId = reminderId
            reminderChanged = false
        }
    }

    fun moveToArchive(dot: Dot) {
        dialogState.value = false
        dot.id?.let { dotId ->
            runInIO {
                reminderCreator.removeReminder(dot)
                val dotDto = dot.toDotDto()
                dotDto.isArchived = true
                dao.update(dotDto)
            }
        }
    }

    fun resetTime(dot: Dot) {
        dialogState.value = false
        runInIO {
            dao.update(dot.toDotDto())
        }
    }

    fun showDatePicker() {
        if (selectedDot.value?.reminder != null) {
            deleteReminderState.value = true
            return
        }
        android.util.Log.v("----------------", "showDatePicker set to true")
        datePickerState.value = true
        reminderChanged = true
    }

    fun removeReminder() {
        selectedDot.value?.run {
            reminder = null
            calendarEventId = null
            calendarReminderId = null
            runInIO {
                reminderCreator.removeReminder(this)
                dao.update(toDotDto())
            }
        }
    }

    fun saveDate(year: Int, month: Int, day: Int) {
        reminderYear = year
        reminderMonth = month
        reminderDay = day
    }

    fun saveTime(hour: Int, minute: Int) {
        with(Calendar.getInstance()) {
            set(Calendar.YEAR, reminderYear)
            set(Calendar.MONTH, reminderMonth)
            set(Calendar.DAY_OF_MONTH, reminderDay)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            selectedDot.value?.reminder = timeInMillis
        }
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
        reminderChanged = false
    }

    fun discardChanges() {
        dialogState.value = false
        selectedDot.value = null
    }

    fun dismissDeleteReminderDialog() {
        deleteReminderState.value = false
    }

    private fun runInIO(block: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                block()
            }
        }
    }
}
