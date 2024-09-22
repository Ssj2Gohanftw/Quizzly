package com.example.quizapp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.quizapp.AuthState
import com.example.quizapp.AuthViewModel
import androidx.compose.material3.ModalNavigationDrawer

@Composable
fun HomePage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel)
{
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
            Button(onClick = { /*TODO*/ }) {
                Text(text="Home")
            }
        }
//   ModalNavigationDrawer(
//       drawerState = drawerState,
//   )

}
