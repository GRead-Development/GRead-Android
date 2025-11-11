package com.gread.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gread.presentation.viewmodels.ActivityViewModel
import com.gread.presentation.viewmodels.AuthViewModel
import com.gread.presentation.viewmodels.GroupsViewModel
import com.gread.presentation.viewmodels.LibraryViewModel
import com.gread.presentation.viewmodels.UserViewModel

enum class TabRoute(val icon: ImageVector, val label: String) {
    ACTIVITY(Icons.Filled.Home, "Activity"),
    LIBRARY(Icons.Filled.LibraryBooks, "Library"),
    GROUPS(Icons.Filled.People, "Groups"),
    PROFILE(Icons.Filled.Person, "Profile")
}

@Composable
fun MainTabScreen(
    authViewModel: AuthViewModel,
    libraryViewModel: LibraryViewModel,
    activityViewModel: ActivityViewModel,
    groupsViewModel: GroupsViewModel,
    userViewModel: UserViewModel,
    userId: Int,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(TabRoute.ACTIVITY) }
    val tabNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                TabRoute.entries.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, tab.label) },
                        label = { Text(tab.label) },
                        selected = selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                            tabNavController.navigate(tab.name) {
                                popUpTo(tabNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = tabNavController,
            startDestination = TabRoute.ACTIVITY.name,
            modifier = androidx.compose.foundation.layout.Modifier.weight(1f)
        ) {
            composable(TabRoute.ACTIVITY.name) {
                ActivityFeedScreen(activityViewModel)
            }
            composable(TabRoute.LIBRARY.name) {
                LibraryScreen(libraryViewModel)
            }
            composable(TabRoute.GROUPS.name) {
                GroupsScreen(groupsViewModel)
            }
            composable(TabRoute.PROFILE.name) {
                ProfileScreen(
                    viewModel = userViewModel,
                    userId = userId,
                    onLogout = onLogout
                )
            }
        }
    }
}
