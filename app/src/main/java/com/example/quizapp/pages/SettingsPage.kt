package com.example.quizapp.pages
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.AuthViewModel

@Composable
fun SettingsPage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel) {
    Column(
        modifier =Modifier.fillMaxSize().background(Color(0xFF3F51B5)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text="Settings Page",
            fontSize=40.sp,
            fontWeight =FontWeight.SemiBold,
            color = Color.White
        )
    }
}