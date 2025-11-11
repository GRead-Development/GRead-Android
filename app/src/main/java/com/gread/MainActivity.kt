package com.gread

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gread.data.api.GReadApiService
import com.gread.data.managers.AuthManager
import com.gread.presentation.viewmodels.*
import com.gread.ui.screens.LoginScreen
import com.gread.ui.screens.MainTabScreen
import com.gread.ui.theme.GReadTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gread.fun/wp-json/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(GReadApiService::class.java)
        val authManager = AuthManager(this, apiService)
        val authViewModel = AuthViewModel(authManager)
        val libraryViewModel = LibraryViewModel(apiService)
        val activityViewModel = ActivityViewModel(apiService)
        val groupsViewModel = GroupsViewModel(apiService)
        val userViewModel = UserViewModel(apiService)

        setContent {
            GReadTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    GReadNavHost(
                        navController = navController,
                        authManager = authManager,
                        authViewModel = authViewModel,
                        libraryViewModel = libraryViewModel,
                        activityViewModel = activityViewModel,
                        groupsViewModel = groupsViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun GReadNavHost(
    navController: NavHostController,
    authManager: AuthManager,
    authViewModel: AuthViewModel,
    libraryViewModel: LibraryViewModel,
    activityViewModel: ActivityViewModel,
    groupsViewModel: GroupsViewModel,
    userViewModel: UserViewModel
) {
    val userId by authViewModel.uiState.collectAsState().value.user?.id?.toString()?.let { remember { androidx.compose.runtime.mutableStateOf(it.toInt()) } } ?: remember { androidx.compose.runtime.mutableStateOf(0) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            val currentUserId = authViewModel.uiState.collectAsState().value.user?.id ?: 0
            MainTabScreen(
                authViewModel = authViewModel,
                libraryViewModel = libraryViewModel,
                activityViewModel = activityViewModel,
                groupsViewModel = groupsViewModel,
                userViewModel = userViewModel,
                userId = currentUserId,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
