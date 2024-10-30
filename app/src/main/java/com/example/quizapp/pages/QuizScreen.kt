// QuizScreen.kt
package com.example.quizapp.pages

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizapp.model.QuizViewModel

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    quizId: String,
    quizViewModel: QuizViewModel = viewModel()
) {
    val questions by quizViewModel.quizQuestions.observeAsState(emptyList())
    var currentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOptionKey by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var hasUsedPowerUp by remember { mutableStateOf(false) } // Tracks power-up usage

    val animatedScore by animateIntAsState(targetValue = score, label = "")
    val currentQuestion = questions.getOrNull(currentIndex)

    LaunchedEffect(quizId) {
        quizViewModel.getQuizQuestions(quizId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Score: $animatedScore",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        currentQuestion?.let { question ->
            // Display the question
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color(0xDD000000), RoundedCornerShape(10.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = question.question_title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Display options with power-up effects
            val optionsToDisplay = if (hasUsedPowerUp) {
                question.options.filter { question.correctAnswers[it.key] == true || it.key == selectedOptionKey }
            } else question.options

            optionsToDisplay.forEach { (optionKey, optionValue) ->
                val isCorrect = question.correctAnswers[optionKey] == true
                val optionColor = when {
                    selectedOptionKey == optionKey && isCorrect && isAnswered -> Color.Green
                    selectedOptionKey == optionKey && !isCorrect && isAnswered -> Color.Red
                    else -> Color.White
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(enabled = selectedOptionKey == null && !isAnswered) {
                            selectedOptionKey = optionKey // Select the option
                        }
                        .background(optionColor, RoundedCornerShape(10.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = optionValue ?: "",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    // Handle selection and scoring
                    if (selectedOptionKey != null) {
                        if (question.correctAnswers[selectedOptionKey] == true) {
                            score += 10
                        }
                        isAnswered = true
                    }
                },
                enabled = !isAnswered && selectedOptionKey != null,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Submit", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Next Question Button
            Button(
                onClick = {
                    currentIndex++
                    selectedOptionKey = null
                    isAnswered = false
                    hasUsedPowerUp = false
                },
                enabled = isAnswered,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
            ) {
                Text("Next Question", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 50-50 Power-Up Button
            Button(
                onClick = {
                    hasUsedPowerUp = true
                },
                enabled = !hasUsedPowerUp,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                Text("Use 50-50 Power-Up", color = Color.White)
            }
        } ?: run {
            // Display quiz completion message and navigate to leaderboard
            Text(
                text = "Quiz Complete! Your final score: $animatedScore",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = { navController.navigate("leaderboard_screen") },
                modifier = Modifier.padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("View Leaderboard", color = Color.White)
            }
        }
    }
}