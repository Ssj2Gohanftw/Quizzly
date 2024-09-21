package com.example.quizapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.pages.HomePage
import com.example.quizapp.pages.LoginPage
import com.example.quizapp.pages.SignUpPage

@Composable
fun Navigation(modifier: Modifier,authViewModel:AuthViewModel){
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination ="login",
        builder = {
            composable("login"){
               LoginPage(modifier,navController,authViewModel)
            }
            composable("home"){
                HomePage(modifier,navController,authViewModel)
            }
            composable("signup"){
                SignUpPage(modifier,navController,authViewModel)
            }
    } )
}