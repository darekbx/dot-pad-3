package com.darekbx.dotpad3.ui.dots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.darekbx.dotpad3.R
import com.darekbx.dotpad3.ui.theme.*
import com.darekbx.dotpad3.utils.TimeUtils

private val colors = listOf(
    dotRed,
    dotTeal,
    dotBlue,
    dotPurple,
    dotOrange,
    dotYellow
)

@Composable
fun DotDialog(
    dot: Dot,
    onSave: (Dot) -> Unit,
    onResetTime: (Dot) -> Unit,
    onAddReminder: (Dot) -> Unit,
    onRemove: (Dot) -> Unit
) {
    Dialog(onDismissRequest = { }) {
        DialogContent(dot, onSave, onResetTime, onAddReminder, onRemove)
    }
}

@Composable
fun DialogContent(
    dot: Dot,
    onSave: (Dot) -> Unit,
    onResetTime: (Dot) -> Unit,
    onAddReminder: (Dot) -> Unit,
    onRemove: (Dot) -> Unit
) {

    val (text, onTextChange) = rememberSaveable { mutableStateOf(dot.text) }
    val submit = {
        dot.text = text
        // TODO presist other values
        onSave(dot)
    }

    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(358.dp)
                .background(Color(0xFF303030))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                DotMessage(text, onTextChange)

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ReminderInfo(dot)
                    StickedInfo(dot)
                }

                DotColors(dot)
                DotSizes(dot)

                if (dot.isNew) {
                    NewDotButtons(submit, onAddReminder, dot)
                } else {
                    EditDotButtons(submit, onResetTime, onAddReminder, onRemove, dot)
                }
            }
        }
    }
}

@Composable
private fun NewDotButtons(
    onSave: () -> Unit,
    onAddReminder: (Dot) -> Unit,
    dot: Dot
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Button(
            modifier = Modifier
                .weight(1F)
                .size(44.dp)
                .padding(end = 4.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotTeal),
            onClick = { onSave() }) { }
        Button(
            modifier = Modifier
                .weight(2F, true)
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotPurple),
            onClick = { onAddReminder(dot) }) { }
    }
}

@Composable
private fun EditDotButtons(
    onSave: () -> Unit,
    onResetTime: (Dot) -> Unit,
    onAddReminder: (Dot) -> Unit,
    onRemove: (Dot) -> Unit,
    dot: Dot
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(
            modifier = Modifier
                .size(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotRed),
            onClick = { onRemove(dot) }) { }
        Button(
            modifier = Modifier
                .width(140.dp)
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotTeal),
            onClick = { onSave() }) { }
        Button(
            modifier = Modifier
                .size(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotOrange),
            onClick = { onResetTime(dot) }) { }
        Button(
            modifier = Modifier
                .size(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotPurple),
            onClick = { onAddReminder(dot) }) { }
    }
}

@Composable
private fun DotColors(dot: Dot) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color),
                contentAlignment = Alignment.TopEnd
            ) {
                if (color == dot.color) {
                    Checkmark()
                }
            }
        }
    }
}

@Composable
private fun DotSizes(dot: Dot) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        DotSize.values().reversed().forEach { size ->
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(44.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF505050)),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 14.dp),
                    text = "${size.size}",
                    style = Typography.h6.copy(color = LightGrey, textAlign = TextAlign.Center),
                    color = Color.LightGray
                )
                if (size == dot.size) {
                    Checkmark()
                }
            }
        }
    }
}

@Composable
private fun Checkmark() {
    Icon(
        modifier = Modifier
            .padding(4.dp)
            .size(12.dp),
        painter = painterResource(id = R.drawable.ic_check),
        tint = Color.White,
        contentDescription = "check_mark"
    )
}

@Composable
private fun StickedInfo(dot: Dot) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = Typography.h6.copy(color = LightGrey),
            text = "Sticked"
        )
        Checkbox(
            modifier = Modifier.padding(start = 4.dp),
            checked = dot.isSticked,
            onCheckedChange = { })
    }
}

@Composable
private fun ReminderInfo(dot: Dot) {
    // TODO set reminder

    val text = if (dot.reminder != null) {
        TimeUtils.formattedDate(dot.reminder)
    } else {
        "No reminder"
    }
    Text(
        style = Typography.h6.copy(color = LightGrey),
        text = text
    )
}

@Composable
private fun DotMessage(text: String, onTextChange: (String) -> Unit) {
    BasicTextField(
        modifier = Modifier
            .height(168.dp)
            .fillMaxWidth()
            .padding(8.dp),
        textStyle = Typography.h6,
        decorationBox = { innerTextField ->
            Row(modifier = Modifier.fillMaxWidth()) {
                if (text.isEmpty()) {
                    Text("Enter note", color = Color.Gray)
                }
            }

            innerTextField()
        },
        value = text,
        onValueChange = onTextChange,
    )
}

@Preview
@Composable
fun DialogPreview() {
    DialogContent(Dot(
        1L, "", 0F, 0F, DotSize.MEDIUM, dotTeal, isSticked = true, createdDate = 1636109037074L,
        reminder = 1636109037074L + 51 * 60 * 1000
    ), { }, { }, { }, { })
}
