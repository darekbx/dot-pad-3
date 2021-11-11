package com.darekbx.dotpad3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.darekbx.dotpad3.navigation.NavigationItem
import com.darekbx.dotpad3.ui.dots.DotsScreen
import com.darekbx.dotpad3.ui.dots.DotsViewModel
import com.darekbx.dotpad3.ui.theme.DotPad3Theme

class MainActivity : ComponentActivity() {

    private val dotsViewModel by viewModels<DotsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            this.window.statusBarColor = android.graphics.Color.BLACK
            DotPad3Theme {
                Scaffold(
                    bottomBar = { BottomAppBar(navController) },
                    content = { Navigation(navController) }
                )
            }
        }
    }

    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController, startDestination = NavigationItem.Home.route) {
            composable(NavigationItem.Home.route) {
                DotsScreen()
            }
            composable(NavigationItem.History.route) {
                HistoryScreen()
            }
            composable(NavigationItem.Statistics.route) {
                StatisticsScreen()
            }
            composable(NavigationItem.Calendar.route) {
                CalendarScreen()
            }
        }
    }

    @Composable
    private fun HistoryScreen() {
        Text("History")
    }

    @Composable
    private fun StatisticsScreen() {
        Text("Statistics")
    }

    @Composable
    private fun CalendarScreen() {
        Text("Calendar")
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
    private fun BottomAppBar(navController: NavController) {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.History,
            NavigationItem.Statistics,
            NavigationItem.Calendar
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Column {
            Separator()
            BottomAppBar {
                BottomNavigation(backgroundColor = Color.Black) {
                    items.forEach { item ->
                        BottomNavigationItem(item, currentRoute, navController)
                    }
                }
            }
        }
    }

    @Composable
    private fun RowScope.BottomNavigationItem(
        item: NavigationItem,
        currentRoute: String?,
        navController: NavController
    ) {
        BottomNavigationItem(
            icon = { Icon(painterResource(id = item.iconResId), contentDescription = item.route) },
            label = { Text(text = stringResource(item.labelResId)) },
            alwaysShowLabel = false,
            selected = currentRoute == item.route,
            onClick = {
                navController.navigate(item.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
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
}
