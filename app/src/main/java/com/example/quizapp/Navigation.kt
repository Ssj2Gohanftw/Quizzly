package com.example.quizapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.pages.*

@Composable
fun Navigation(modifier: Modifier, authViewModel: AuthViewModel,context: Context) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "roleSelection",
        builder = {
            // Role selection page
            composable("roleSelection") {
                RoleSelectionPage(navController = navController)
            }

            // Student login and home
            composable("login") {
                LoginPage(modifier, navController, authViewModel)
            }
            composable("signup") {
                SignUpPage(modifier, navController, authViewModel)
            }
            composable("home") {
                HomePage(modifier, navController, authViewModel,context)
            }

            // Teacher login and home
            composable("trlogin") {
                TrLoginPage(modifier, navController, authViewModel)
            }
            composable("trsignup") {
                TrSignUpPage(modifier, navController, authViewModel)
            }
            composable("trhome") {
                TrHomePage(modifier, navController, authViewModel,context)
            }

            composable("currentscreen") {
                CurrentScreen(modifier, navController, authViewModel,context)
            }
            composable("trcurrentscreen") {
                TrCurrentScreen(modifier, navController, authViewModel,context)
            }

            // Classes and settings pages
            composable("classes") {
                ClassesPage(modifier, navController, authViewModel,context)
            }
            composable("settings") {
                SettingsPage(modifier, navController, authViewModel,context)
            }
            composable("leaderboards") {
                SettingsPage(modifier, navController, authViewModel,context)
            }
            // Quiz screen with parameters for quiz details
            composable("quiz_screen/{quizId}") { backStackEntry ->
                val quizId = backStackEntry.arguments?.getString("quizId") ?: ""

                QuizScreen(
                    modifier = modifier,
                    navController = navController,
                    quizId = quizId
                )
            }
        }
    )
}
