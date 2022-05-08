package com.mabahmani.instasave.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mabahmani.instasave.ui.main.download.DownloadScreen
import com.mabahmani.instasave.ui.main.home.HomeScreen
import com.mabahmani.instasave.ui.main.livestream.LiveStreamScreen
import com.mabahmani.instasave.ui.main.search.SearchScreen
import com.mabahmani.instasave.ui.main.story.StoryScreen
import com.mabahmani.instasave.ui.theme.InstaSaveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaSaveTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
            composable(BottomNavItem.Home.screenRoute) {
                HomeScreen()
            }
            composable(BottomNavItem.Search.screenRoute) {
                SearchScreen()
            }
            composable(BottomNavItem.Download.screenRoute) {
                DownloadScreen()
            }
            composable(BottomNavItem.Story.screenRoute) {
                StoryScreen()
            }
            composable(BottomNavItem.LiveStream.screenRoute) {
                LiveStreamScreen()
            }
        }
    }

    @Composable
    fun BottomNavigation(navController: NavController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Search,
            BottomNavItem.Download,
            BottomNavItem.Story,
            BottomNavItem.LiveStream
        )
        androidx.compose.material.BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary
        ) {

            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->

                val selected = currentRoute == item.screenRoute

                BottomNavigationItem(
                    icon = {
                        Icon(
                            if (selected) painterResource(id = item.iconSolid) else painterResource(
                                id = item.iconLight
                            ),
                            contentDescription = item.title,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp),
                        )
                    },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.primary,
                    alwaysShowLabel = false,
                    selected = selected,
                    onClick = {
                        navController.navigate(item.screenRoute) {

                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController) }
        ) {
            Box(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, it.calculateBottomPadding())) {
                NavigationGraph(navController = navController)
            }
        }
    }
}

