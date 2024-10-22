package com.example.quizapp.pages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.quizapp.AuthState
import com.example.quizapp.AuthViewModel

@Composable
fun SettingsPage(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val currentUser = authViewModel.getCurrentUser()

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            profileImageUri = uri
        }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    // Use LazyColumn for scrollable content
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(100.dp))

            // Card-like Background for Profile Picture and Username
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(150.dp)) {
                        profileImageUri?.let {
                            Image(
                                painter = rememberAsyncImagePainter(it),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(150.dp)
                                    .align(Alignment.Center)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                        } ?: run {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Profile Icon",
                                modifier = Modifier
                                    .size(150.dp)
                                    .align(Alignment.Center)
                                    .padding(8.dp),
                                tint = Color.Gray
                            )
                        }

                        // Edit button at the top right corner
                        IconButton(
                            onClick = { isEditing = !isEditing },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Username Display
                    Text(
                        text = currentUser?.displayName ?: "Username",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // If edit mode is on, show edit options
            if (isEditing) {
                // Change Profile Picture Button
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Change Profile Picture", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // When an image is picked, upload it to Firebase Storage
                profileImageUri?.let {
                    LaunchedEffect(it) {
                        authViewModel.uploadProfilePicture(context, it)
                    }
                }

                // Update Username Button
                Button(
                    onClick = { authViewModel.updateUsername("New Username") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Change Username", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Update Password Button
                Button(
                    onClick = { authViewModel.updatePassword("new_password") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Change Password", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Delete Account Button
        item {
            Spacer(modifier = Modifier.height(16.dp)) // Adjust the height as needed
            Button(
                onClick = { authViewModel.deleteAccount() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Delete Account", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp)) // Adjust the height as needed
        }

// Sign out Button
        item {
            Button(
                onClick = { authViewModel.signout() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Sign out", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp)) // Adjust the height as needed
        }
    }
}

