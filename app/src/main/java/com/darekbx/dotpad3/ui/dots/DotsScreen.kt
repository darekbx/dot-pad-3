package com.darekbx.dotpad3.ui.dots

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darekbx.dotpad3.ui.theme.dotRed
import com.darekbx.dotpad3.utils.TimeUtils
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun DotsBoardScreen(
    items: List<Dot>,
    selectedDot: Dot?,
    onSave: (Dot) -> Unit,
    onResetTime: (Dot) -> Unit,
    onAddReminder: (Dot) -> Unit,
    onRemove: (Dot) -> Unit,
    onSelectDot: (Dot) -> Unit,
    dotDialogState: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        val dot = Dot(null, "", offset.x, offset.y, DotSize.MEDIUM, dotRed)
                        onSelectDot(dot)
                    }
                )
            }
    ) {
        DrawAreas()
        DotsBoard(items = items, onRemove, onSelectDot)
    }

    if (dotDialogState && selectedDot != null) {
        DotDialog(selectedDot, onSave, onResetTime, onAddReminder, onRemove)
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
fun DotsBoard(items: List<Dot>, onRemove: (Dot) -> Unit, onSelectDot: (Dot) -> Unit) {
    for (item in items) {
        DotView(item,
            onRemove = { onRemove(item) },
            onSelectDot = { onSelectDot(it) }
        )
    }
}

@Composable
fun DotView(dot: Dot, onRemove: () -> Unit, onSelectDot: (Dot) -> Unit) {
    val offsetX = rememberSaveable(dot) { mutableStateOf(dot.x) }
    val offsetY = rememberSaveable(dot) { mutableStateOf(dot.y) }
    val size = mapDotSize(dot)

    Box(
        Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .clip(CircleShape)
            .alpha(if (dot.isSticked) 0.5F else 1F)
            .background(dot.color)
            .size(size)
            .pointerInput(dot) {
                detectTapGestures(
                    onTap = { onSelectDot(dot) },
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
    val timeAgo = rememberSaveable(dot) { TimeUtils.calculateTimeAgo(dot.createdDate) }
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

@Preview
@Composable
fun DotPreview() {
    DotView(Dot(1L,"One", 0F, 0F, DotSize.LARGE, dotRed, isSticked = false,
        createdDate = 1636109037074L,
        reminder = 1636109037074L + 51 * 60 * 1000), onRemove = { }, { })
}