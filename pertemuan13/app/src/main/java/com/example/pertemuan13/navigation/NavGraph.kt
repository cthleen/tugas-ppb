package com.example.pertemuan13.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pertemuan13.ui.screens.IntroScreen
import com.example.pertemuan13.ui.screens.RegistrasiScreen
import com.example.pertemuan13.ui.screens.SplashScreen
import com.example.pertemuan13.viewmodel.SiswaViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Intro : Screen("intro")
    object Registrasi : Screen("registrasi")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: SiswaViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.Intro.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Intro.route) {
            IntroScreen(
                onGetStarted = {
                    navController.navigate(Screen.Registrasi.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Registrasi.route) {
            RegistrasiScreen(viewModel = viewModel)
        }
    }
}
