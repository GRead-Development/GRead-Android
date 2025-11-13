package com.gread.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gread.presentation.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    userId: Int,
    onLogout: () -> Unit
) {
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        }
        uiState.user != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (uiState.user!!.avatar.isNotBlank()) {
                    AsyncImage(
                        model = uiState.user!!.avatar,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                val displayName = uiState.user!!.displayName.ifBlank { uiState.user!!.username }
                Text(
                    displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                if (uiState.user!!.username.isNotBlank()) {
                    Text(
                        "@${uiState.user!!.username}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.stats != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard("Books Read", uiState.stats!!.booksRead.toString())
                        StatCard("Pages Read", uiState.stats!!.pagesRead.toString())
                        StatCard("Points", uiState.stats!!.totalPoints.toString())
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.labelLarge)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

