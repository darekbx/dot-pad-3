package com.darekbx.dotpad3.ui.dots

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun DotsBoardScreen(
    items: List<Dot>,
    onSaveItem: (Dot) -> Unit,
    onRemoveItem: (Dot) -> Unit,
    onShowDotDialog: (Dot?) -> Unit,
    dotDialogState: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        onShowDotDialog(null)
                    }
                )
            }
    ) {
        DrawAreas()
        DotsBoard(items = items, onRemoveItem)
    }

    if (dotDialogState) {
        DotDialog(onSaveItem, onRemoveItem)
    }
}

@Composable
private fun DrawAreas() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val position = Offset(0F, 0F)
        val areaSize = 750F
        val stroke = Stroke(
            width = 4f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), 0f)
        )
        for (i in 1..3) {
            drawCircle(Color.DarkGray, radius = areaSize * i, position, style = stroke)
        }
    }
}

@Composable
fun DotsBoard(items: List<Dot>, onRemoveItem: (Dot) -> Unit) {
    for (item in items) {
        DotView(item, onRemove = {
            onRemoveItem(item)
        })
    }
}

@Composable
fun DotView(dot: Dot, onRemove: () -> Unit) {
    val offsetX = rememberSaveable(dot) { mutableStateOf(dot.x) }
    val offsetY = rememberSaveable(dot) { mutableStateOf(dot.y) }
    val size = mapDotSize(dot)

    val context = LocalContext.current

    Box(
        Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .clip(CircleShape)
            .background(dot.color)
            .size(size)
            .pointerInput(dot) {
                detectTapGestures(
                    onTap = {
                        Toast
                            .makeText(context, dot.text, Toast.LENGTH_SHORT)
                            .show()
                    },
                    onLongPress = { onRemove() }
                )
            }
            .pointerInput(dot) {
                detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX.value += dragAmount.x
                    offsetY.value += dragAmount.y
                    dot.x = offsetX.value
                    dot.y = offsetY.value
                }
            }, contentAlignment = Alignment.Center
    ) {
        DotReminder(dot)
        DotText(dot)
    }
}

@Composable
private fun DotText(dot: Dot) {
    val timeAgo = rememberSaveable(dot) {calculateTimeAgo(dot.createdDate)}
    Text(text = timeAgo, color = Color.White, fontSize = 5.sp)
}

@Composable
private fun DotReminder(dot: Dot) {
    if (dot.reminder != null) {
        val now = System.currentTimeMillis()
        val remianingSpan = (dot.reminder - now)
        val span = (dot.reminder - dot.createdDate)
        val percent = (span - remianingSpan).toFloat() / max(1, span)
        CircularProgressIndicator(
            progress = percent,
            color = Color.White,
            strokeWidth = 1.5.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
        )
    }
}

private fun mapDotSize(dot: Dot) = when (dot.size) {
    DotSize.SMALL -> 20.dp
    DotSize.MEDIUM -> 35.dp
    DotSize.LARGE -> 60.dp
    DotSize.HUGE -> 80.dp
}

private fun calculateTimeAgo(createdTime: Long): String {
    val currentTime = System.currentTimeMillis()
    val diff = (currentTime - createdTime)
    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "${TimeUnit.MILLISECONDS.toSeconds(diff)}s"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h"
        else -> "${TimeUnit.MILLISECONDS.toDays(diff)}d"
    }
}

@Preview
@Composable
fun DotPreview() {
    DotView(Dot(1L,"One", 0F, 0F, DotSize.LARGE, Color(249, 168, 37)), onRemove = { })
}