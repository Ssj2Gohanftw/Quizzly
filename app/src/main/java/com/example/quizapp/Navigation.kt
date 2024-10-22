package com.example.quizapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.components.QuizViewModel
import com.example.quizapp.pages.ClassesPage
import com.example.quizapp.pages.HomePage
import com.example.quizapp.pages.RoleSelectionPage
import com.example.quizapp.pages.LoginPage
//import com.example.quizapp.pages.QuizScreen
import com.example.quizapp.pages.SettingsPage
import com.example.quizapp.pages.SignUpPage
import com.example.quizapp.pages.TrHomePage
import com.example.quizapp.pages.TrLoginPage
import com.example.quizapp.pages.TrSignUpPage


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
            composable("trlogin"){
                TrLoginPage(modifier,navController,authViewModel)
            }
            composable("trhome"){
                TrHomePage(modifier,navController,authViewModel)
            }
            composable("trsignup"){
                TrSignUpPage(modifier,navController,authViewModel)
            }

            composable("home"){
                HomePage(modifier,navController,authViewModel)
            }
            composable("signup"){
                SignUpPage(modifier,navController,authViewModel)
            }
            composable("quiz_screen/{quizId}/{category}/{difficulty}") { backStackEntry ->
                val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
                val category = backStackEntry.arguments?.getString("category") ?: ""
                val difficulty = backStackEntry.arguments?.getString("difficulty") ?: ""
//
//                QuizScreen(
//                    modifier = Modifier,
//                    quizId = quizId.toInt(),
//                    category = category,
//                    difficulty = difficulty
//                )
            }
            composable("currentscreen"){
                CurrentScreen(modifier,navController,authViewModel)
            }
            composable("trcurrentscreen"){
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