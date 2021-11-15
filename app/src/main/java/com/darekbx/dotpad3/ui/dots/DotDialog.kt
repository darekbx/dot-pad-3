package com.darekbx.dotpad3.ui.dots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.darekbx.dotpad3.ui.theme.*

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
    onSaveItem: (Dot) -> Unit,
    onRemoveItem: (Dot) -> Unit
) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        DialogContent(
            Dot(null,"", 0F, 0F, DotSize.MEDIUM, Color.Black),
            onSaveItem,
            onRemoveItem
        )
    }
}

@Composable
fun DialogContent(
    dot: Dot,
    onSaveItem: (Dot) -> Unit,
    onRemoveItem: (Dot) -> Unit
) {
    var dotText by remember { mutableStateOf("One\nTwo\nThree\nFour\nFive\nSix\nSeven\nEight\nNine\nTen\nEleven") }
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
                DotMessage(dot)

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
                    NewDotButtons(onSaveItem, dot)
                } else {
                    EditDotButtons(onSaveItem, dot)
                }
            }

        }
    }
}

@Composable
private fun NewDotButtons(
    onSaveItem: (Dot) -> Unit,
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
            onClick = { onSaveItem(dot) }) { }
        Button(
            modifier = Modifier
                .weight(2F, true)
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotPurple),
            onClick = { onSaveItem(dot) }) { }
    }
}

@Composable
private fun EditDotButtons(
    onSaveItem: (Dot) -> Unit,
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
            onClick = { onSaveItem(dot) }) { }
        Button(
            modifier = Modifier
                .width(140.dp)
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotTeal),
            onClick = { onSaveItem(dot) }) { }
        Button(
            modifier = Modifier
                .size(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotOrange),
            onClick = { onSaveItem(dot) }) { }
        Button(
            modifier = Modifier
                .size(44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = dotPurple),
            onClick = { onSaveItem(dot) }) { }
    }
}

@Composable
private fun DotColors(dot: Dot) {
    // TODO select existing dot color
    // TODO mark selected color
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
                    .background(color)
            )
        }
    }
}

@Composable
private fun DotSizes(dot: Dot) {
    // TODO select existing dot size
    // TODO mark selected size
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
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${size.size}",
                    style = Typography.h6.copy(color = LightGrey),
                    color = Color.LightGray
                )
            }
        }
    }
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
    Text(
        style = Typography.h6.copy(color = LightGrey),
        text = "No reminder"
    )
}

@Composable
private fun DotMessage(dot: Dot) {
    BasicTextField(
        modifier = Modifier
            .height(168.dp)
            .fillMaxWidth()
            .padding(8.dp),
        textStyle = Typography.h6,
        decorationBox = { innerTextField ->
            Row(modifier = Modifier.fillMaxWidth()) {
                if (dot.text.isEmpty()) {
                    Text("Enter note", color = Color.Gray)
                }
            }

            innerTextField()
        },
        value = dot.text,
        onValueChange = { dot.text = it },
    )
}

@Preview
@Composable
fun DialogPreview() {
    DialogContent(Dot(1L,"", 0F, 0F, DotSize.MEDIUM, Color.Black), { }, { })
}