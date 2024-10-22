package com.example.quizapp.pages
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.quizapp.components.QuizQuestionCard
//import com.example.quizapp.components.QuizViewModel
//
//@Composable
//fun QuizScreen(
//    modifier: Modifier,
//    quizId: Int,
//    category: String,  // Pass category dynamically
//    difficulty: String  // Pass difficulty dynamically
//) {
//    // Get QuizViewModel locally inside QuizScreen
//    val quizViewModel: QuizViewModel = viewModel()
//    val questions by quizViewModel.quizQuestions.observeAsState(emptyList())
//
//    // Fetch questions based on quizId, category, and difficulty
//    LaunchedEffect(quizId, category, difficulty) {
//        quizViewModel.getQuizQuestions(
//            apiKey = "83o5JIEmD0qK1M8aBpxF17AUzPP1bPBxFxsFZCLb",
//            limit = 10,  // For example, 10 questions per quiz
//            category = category,  // Dynamic category
//            difficulty = difficulty  // Dynamic difficulty
//        )
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        questions.forEach { question ->
//            QuizQuestionCard(question)
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}