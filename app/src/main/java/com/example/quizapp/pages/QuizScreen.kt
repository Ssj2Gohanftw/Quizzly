package com.example.quizapp.pages
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.sharp.Bolt
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizapp.R
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.model.LeaderboardViewModel
import com.example.quizapp.model.QuizViewModel
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    quizId: String,
    quizViewModel: QuizViewModel = viewModel(),
    leaderboardViewModel: LeaderboardViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val currentUser = authViewModel.getCurrentUser()
    val userId = currentUser?.uid ?: return
    val questions by quizViewModel.quizQuestions.observeAsState(emptyList())
    var isScoreUpdated by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOptionKey by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var hasUsedPowerUp by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }
    val animatedScore by animateIntAsState(targetValue = score, label = "")
    val currentQuestion = questions.getOrNull(currentIndex)
    var showQuitDialog by remember { mutableStateOf(false) }
    LaunchedEffect(quizId) {
        quizViewModel.getQuizQuestions(quizId)
    }

    BackHandler {
        showQuitDialog = true
    }
    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            title = { Text("Quit Quiz?") },
            text = { Text("Are you sure you want to quit?") },
            confirmButton = {
                Button(
                    onClick = {
                        showQuitDialog = false
                        navController.navigate("currentscreen")
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showQuitDialog = false }) {
                    Text("No")
                }
            }
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentIndex < questions.size) {
                Text(
                    text = "${currentIndex + 1}/${questions.size}",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            currentQuestion?.let { question ->
                // Question Card
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

                // Determine options based on power-up usage
                val optionsToDisplay = if (hasUsedPowerUp) {
                    // Filter options to display one correct and one incorrect option
                    val correctOptions = question.options.filterKeys { question.correctAnswers[it] == true }
                    val incorrectOptions = question.options.filterKeys { question.correctAnswers[it] == false }

                    // Get only one correct and one incorrect option
                    if (correctOptions.isNotEmpty() && incorrectOptions.isNotEmpty()) {
                        mapOf(
                            correctOptions.entries.first().toPair(),
                            incorrectOptions.entries.first().toPair()
                        )
                    } else {
                        question.options // Fallback in case filtering fails
                    }
                } else {
                    question.options
                }

                optionsToDisplay.forEach { (optionKey, optionValue) ->
                    val isCorrect = question.correctAnswers[optionKey] == true
                    val optionColor = when {
                        isAnswered && optionKey == selectedOptionKey && isCorrect -> Color.Green
                        isAnswered && optionKey == selectedOptionKey && !isCorrect -> Color.Red
                        else -> Color.White
                    }

                    // Option Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(optionColor, RoundedCornerShape(20.dp))
                            .clickable(enabled = !isAnswered) {
                                selectedOptionKey = if (selectedOptionKey == optionKey) null else optionKey
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        if (optionValue != null) {
                            Text(
                                text = optionValue,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(16.dp),
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (selectedOptionKey != null) {
                            if (question.correctAnswers[selectedOptionKey] == true) {
                                score += question.points
                                isScoreUpdated=true
                            }
                            else{
                                isScoreUpdated=false
                            }
                            showFeedback=true
                            isAnswered = true
                    }},
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
                        showFeedback = false
                        isScoreUpdated=false
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
                    onClick = { hasUsedPowerUp = true },
                    enabled = !hasUsedPowerUp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 10.dp)
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = RoundedCornerShape(15)
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Bolt,
                        contentDescription = "Power-Up",
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Use 50-50 Power-Up",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                AnimatedVisibility(visible = showFeedback) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center // Center the feedback card within the screen
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isScoreUpdated) Color.Green else Color.Red
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.8f) // Set a width to make it look centered horizontally
                                .padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = if (isScoreUpdated) Icons.Default.CheckCircle else Icons.Sharp.Clear,
                                    contentDescription = "Feedback Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isScoreUpdated) "+${question.points} Correct Answer!" else "Incorrect Answer!",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }?: run {
                // Quiz Completion Message
                Text(
                    text = "Quiz Complete! Your final score: $animatedScore",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
                scope.launch {
                    leaderboardViewModel.savePlayerScore(
                        quizId = quizId,
                        userId = userId,
                        score = score,
                    )
                }
                Button(
                    onClick = { navController.navigate("leaderboards/$quizId") },
                    modifier = Modifier.padding(top = 24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("View Leaderboard", color = Color.White)
                }
                Button(
                    onClick = { navController.navigate("currentscreen") },
                    modifier = Modifier.padding(top = 24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Home", color = Color.White)
                }
            }
        }
    }
}