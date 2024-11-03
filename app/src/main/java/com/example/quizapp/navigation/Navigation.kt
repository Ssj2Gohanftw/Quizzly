package com.example.quizapp.navigation
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.model.LeaderboardViewModel
import com.example.quizapp.model.QuizViewModel
import com.example.quizapp.pages.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation(modifier: Modifier, authViewModel: AuthViewModel,context: Context) {
    val navController = rememberNavController()
    val userId = authViewModel.getCurrentUser()?.uid
   val leaderboardViewModel: LeaderboardViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null){
        "currentScreen"
    }
    else{
        "roleSelection"
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
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
            composable("leaderboards"){
                QuizzesLeaderboardsScreen(leaderboardViewModel = leaderboardViewModel,navController = navController)
            }

            composable("leaderboards/{quizId}") { backStackEntry ->
                val quizId = backStackEntry.arguments?.getString("quizId") ?: return@composable
                if (userId != null) {
                    LeaderboardScreen(
                        leaderboardViewModel = leaderboardViewModel,
                        quizId = quizId,
                        userId = userId
                    )
                }
            }
            // Quiz screen with parameters for quiz details
            composable("quiz_screen/{quizId}") { backStackEntry ->
                val quizId = backStackEntry.arguments?.getString("quizId") ?: ""

                QuizScreen(
                    modifier = modifier,
                    navController = navController,
                    quizId = quizId,
                    quizViewModel = quizViewModel,
                    leaderboardViewModel = leaderboardViewModel
                )
            }
        }
    )
}