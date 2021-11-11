package com.darekbx.dotpad3.ui.dots

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DotDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.height(200.dp).width(300.dp)) {

            }
        }
    }
}