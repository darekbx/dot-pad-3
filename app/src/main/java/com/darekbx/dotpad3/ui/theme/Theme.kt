package com.darekbx.dotpad3.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorPalette = lightColors(
    primary = Color.Black,
    primaryVariant = LightGreen,
    secondary = Teal200
)

@Composable
fun DotPad3Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = ColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}