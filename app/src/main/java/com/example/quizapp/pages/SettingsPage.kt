package com.example.quizapp.pages
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        profileImageUri = uri
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))



        Button(
            onClick = {
                pickImageLauncher.launch("image/*")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Change Profile Picture", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // When an image is picked, upload it to Firebase Storage
        profileImageUri?.let {
            LaunchedEffect(it) {
                authViewModel.uploadProfilePicture(context, it) // Pass context here
            }
        }

        // Update Username Button
        Button(
            onClick = {
                // Trigger username update logic
                authViewModel.updateUsername("New Username") // You can prompt the user for a new username
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Change Username", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Update Password Button
        Button(
            onClick = {
                // Trigger password update logic
                authViewModel.updatePassword("new_password") // You can prompt for the new password
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Change Password", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delete Account Button
        Button(
            onClick = {
                // Trigger account deletion logic
                authViewModel.deleteAccount()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Delete Account", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign out Button
        Button(
            onClick = { authViewModel.signout() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Sign out", color = Color.White)
        }
    }
}