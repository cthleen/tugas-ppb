package com.example.pertemuan14.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pertemuan14.ui.screen.BookmarkScreen
import com.example.pertemuan14.ui.screen.DetailScreen
import com.example.pertemuan14.ui.screen.HomeScreen
import com.example.pertemuan14.ui.theme.NewsDarkSurface
import com.example.pertemuan14.ui.theme.NewsGray
import com.example.pertemuan14.ui.theme.NewsWhite
import com.example.pertemuan14.ui.viewmodel.NewsViewModel

sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "Home")
    object Bookmarks : Screen("bookmarks", "Bookmarks")
    object Detail : Screen("detail", "Detail")
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val viewModel: NewsViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show bottom nav only on main tabs, not on detail
    val showBottomBar = currentRoute in listOf(Screen.Home.route, Screen.Bookmarks.route)

    Scaffold(
        containerColor = com.example.pertemuan14.ui.theme.NewsBlack,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar(
                    containerColor = NewsDarkSurface,
                    tonalElevation = 0.dp
                ) {
                    val homeSelected = currentRoute == Screen.Home.route
                    val bookmarkSelected = currentRoute == Screen.Bookmarks.route

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (homeSelected) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text(
                                "Home",
                                fontSize = 11.sp,
                                fontWeight = if (homeSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        selected = homeSelected,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NewsWhite,
                            selectedTextColor = NewsWhite,
                            unselectedIconColor = NewsGray,
                            unselectedTextColor = NewsGray,
                            indicatorColor = Color(0xFF2A2A2A)
                        )
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (bookmarkSelected) Icons.Filled.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmarks"
                            )
                        },
                        label = {
                            Text(
                                "Bookmarks",
                                fontSize = 11.sp,
                                fontWeight = if (bookmarkSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        selected = bookmarkSelected,
                        onClick = {
                            navController.navigate(Screen.Bookmarks.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NewsWhite,
                            selectedTextColor = NewsWhite,
                            unselectedIconColor = NewsGray,
                            unselectedTextColor = NewsGray,
                            indicatorColor = Color(0xFF2A2A2A)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onArticleClick = { article ->
                        viewModel.selectArticle(article)
                        navController.navigate(Screen.Detail.route)
                    }
                )
            }

            composable(Screen.Bookmarks.route) {
                BookmarkScreen(
                    viewModel = viewModel,
                    onArticleClick = { article ->
                        viewModel.selectArticle(article)
                        navController.navigate(Screen.Detail.route)
                    }
                )
            }

            composable(Screen.Detail.route) {
                val article by viewModel.selectedArticle.collectAsState()
                article?.let { a ->
                    DetailScreen(
                        article = a,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
