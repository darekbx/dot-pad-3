package com.darekbx.dotpad3.navigation

import com.darekbx.dotpad3.R

sealed class NavigationItem(var route: String, var labelResId: Int, val iconResId: Int) {
    object Home: NavigationItem("home", R.string.home, R.drawable.ic_home)
    object History: NavigationItem("history", R.string.history, R.drawable.ic_history)
    object Statistics: NavigationItem("statistics", R.string.statistics, R.drawable.ic_pie_chart)
    object Calendar: NavigationItem("calendar", R.string.calendar, R.drawable.ic_calendar)
}