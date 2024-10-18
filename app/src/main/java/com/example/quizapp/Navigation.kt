package com.example.quizapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.pages.ClassesPage
import com.example.quizapp.pages.HomePage
import com.example.quizapp.pages.RoleSelectionPage
import com.example.quizapp.pages.LoginPage
import com.example.quizapp.pages.SettingsPage
import com.example.quizapp.pages.SignUpPage


@Composable
fun Navigation(modifier: Modifier,authViewModel:AuthViewModel){
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination ="roleSelection",
        builder = {
            composable("roleSelection"){
                RoleSelectionPage(navController = navController)
            }
            composable("login"){
                LoginPage(modifier,navController,authViewModel)
            }
//            composable("teacherLogin") {
//                TeacherLoginPage(modifier,navController,authViewModel)
//            }
            composable("home"){
                HomePage(modifier,navController,authViewModel)
            }
            composable("signup"){
                SignUpPage(modifier,navController,authViewModel)
            }
            composable("currentscreen"){
                CurrentScreen(modifier,navController,authViewModel)
            }
             composable("classes") {
                 ClassesPage(modifier, navController,authViewModel)
             }
            composable("settings") {
                SettingsPage(modifier, navController, authViewModel)
            }
    }
    )
}