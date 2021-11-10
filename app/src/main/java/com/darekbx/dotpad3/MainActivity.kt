package com.darekbx.dotpad3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.darekbx.dotpad3.ui.dots.DotsScreen
import com.darekbx.dotpad3.ui.dots.DotsViewModel
import com.darekbx.dotpad3.ui.theme.DotPad3Theme

class MainActivity : ComponentActivity() {

    private val dotsViewModel by viewModels<DotsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            this.window.statusBarColor = android.graphics.Color.BLACK
            DotPad3Theme {
                Scaffold(
                    bottomBar = { AppBar() },
                    content = { DotsScreen() }
                )
            }
        }
    }

    @Composable
    private fun DotsScreen() {
        DotsScreen(
            items = dotsViewModel.dots,
            onAddItem = dotsViewModel::addItem,
            onRemoveItem = dotsViewModel::removeItem
        )
    }

    @Composable
    private fun AppBar() {
        Column {
            Separator()
            BottomAppBar {
                BottomNavigation(backgroundColor = Color.Black) {
                    NavigationItem(R.string.history, R.drawable.ic_history) {
                        // TODO go to history
                    }
                    NavigationItem(R.string.statistics, R.drawable.ic_pie_chart) {
                        // TODO go to statistics
                    }
                    NavigationItem(R.string.calendar, R.drawable.ic_calendar) {
                        // TODO go to calendar
                    }
                }
            }
        }
    }

    @Composable
    private fun Separator() {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        ) {
            drawLine(Color.DarkGray, Offset(0F, 0F), Offset(size.width, 0F), strokeWidth = 2F)
        }
    }

    @Composable
    private fun RowScope.NavigationItem(labelResId: Int, iconResId: Int, callback: () -> Unit) {
        BottomNavigationItem(
            onClick = { callback() },
            icon = {
                Icon(
                    painterResource(id = iconResId),
                    contentDescription = stringResource(id = labelResId)
                )
            },
            label = { Text(text = stringResource(id = labelResId)) },
            selected = false,
            alwaysShowLabel = true
        )
    }
}
