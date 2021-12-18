package com.darekbx.dotpad3.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.darekbx.dotpad3.ui.CommonLoading
import com.darekbx.dotpad3.ui.history.HistoryDot
import com.darekbx.dotpad3.ui.dots.Dot
import com.darekbx.dotpad3.ui.theme.Typography

@ExperimentalFoundationApi
@Composable
fun ListScreen(dots: State<List<Dot>?>) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(bottom = 58.dp)
    ) {
        dots.value?.let { dotsList ->
            DotList(dotsList)
        } ?:  CommonLoading()
    }
}

@Composable
private fun DotList(dots: List<Dot>) {
    if (dots.isEmpty()) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Nothing to show",
                style = Typography.h5
            )
        }
    } else {
        LazyColumn(Modifier.fillMaxWidth()) {
            items(dots) { dot ->
                HistoryDot(dot) { /* Do nothing on click */ }
                Divider(color = Color.DarkGray)
            }
        }
    }
}
