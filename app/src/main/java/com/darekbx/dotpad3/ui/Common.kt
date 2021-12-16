package com.darekbx.dotpad3.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darekbx.dotpad3.ui.dots.toColor
import com.darekbx.dotpad3.ui.theme.dotOrange

@Composable
fun CommonLoading() {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        CircularProgressIndicator(
            Modifier
                .align(Alignment.Center)
                .size(64.dp),
            color = dotOrange.toColor()
        )
    }
}