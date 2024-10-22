package com.example.quizapp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizapp.model.QuizQuestion
import com.example.quizapp.model.QuizQuestionCard
import com.example.quizapp.model.QuizViewModel
import com.example.quizapp.model.loadQuizQuestions

@Composable
fun QuizScreen(
    modifier: Modifier,
    navController: NavController,
    quizId: String,  // Use quizId as String
    category: String,  // Category (like "Linux", "OOP", etc.)
    difficulty: String  // Difficulty (like "Easy", "Medium", etc.)
) {
    var questions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch questions from Firebase when quizId changes
    LaunchedEffect(quizId) {
        loadQuizQuestions(quizId) { loadedQuestions ->
            questions = loadedQuestions
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            // Display loading indicator while questions are being fetched
            Text(text = "Loading questions...", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (questions.isNotEmpty()) {
            // Display the list of questions
            questions.forEach { question ->
                QuizQuestionCard(question)
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            // Display message if no questions are found
            Text(text = "No questions available for this quiz.", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
