package com.example.pertemuan12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pertemuan12.data.AppDatabase
import com.example.pertemuan12.data.UserRepository
import com.example.pertemuan12.ui.AuthViewModel
import com.example.pertemuan12.ui.AuthViewModelFactory
import com.example.pertemuan12.ui.screens.HomeScreen
import com.example.pertemuan12.ui.screens.LoginScreen
import com.example.pertemuan12.ui.screens.RegisterScreen
import com.example.pertemuan12.ui.theme.Pertemuan12Theme

// Navigation routes
object Routes {
    const val LOGIN    = "login"
    const val REGISTER = "register"
    const val HOME     = "home"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Tahap 5: Inisialisasi Database dan Repository
        val database   = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(database.userDao())
        val factory    = AuthViewModelFactory(repository)

        setContent {
            Pertemuan12Theme(dynamicColor = false) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(factory = factory)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(factory: AuthViewModelFactory) {
    val navController = rememberNavController()
    // Single shared ViewModel for both login & register
    val authViewModel: AuthViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    // After register, go back to login
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}