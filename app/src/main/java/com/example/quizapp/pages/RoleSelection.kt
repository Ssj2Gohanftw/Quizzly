package com.example.quizapp.pages
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RoleSelectionPage(navController:NavController) {
    Column(modifier=Modifier.fillMaxSize().padding(16.dp),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Select Role", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button( colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),onClick = { navController.navigate("login") }) {
            Text("I'm a Student",color= Color.White)
        }
        Spacer(Modifier.height(16.dp))
        Button( colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),onClick = { navController.navigate("trlogin") }) {
            Text("I'm a Teacher",color= Color.White)
        }
    }
}