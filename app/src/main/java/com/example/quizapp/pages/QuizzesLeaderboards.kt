package com.example.quizapp.pages
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.R
import com.example.quizapp.model.LeaderboardViewModel
import com.example.quizapp.model.QuizLeaderboard

@Composable
fun QuizzesLeaderboardsScreen(
    navController: NavController,
    leaderboardViewModel: LeaderboardViewModel
) {
    val quizzes by leaderboardViewModel.quizzes.collectAsState()
    var selectedQuizIndex by remember { mutableStateOf(0) }
    var fadeInAlpha by remember { mutableStateOf(0f) }

    LaunchedEffect(selectedQuizIndex) {
        // Smoothly fade in the leaderboard when switching quizzes
        fadeInAlpha = 0f
        fadeInAlpha = 1f
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(32.dp))

            if (quizzes.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedQuizIndex,
                    edgePadding = 16.dp,
                    containerColor = Color.Transparent, // Makes the tab row background transparent
                    divider = {}
                ) {
                    quizzes.forEachIndexed { index, quiz ->
                        Tab(
                            selected = selectedQuizIndex == index,
                            onClick = {
                                selectedQuizIndex = index
                                fadeInAlpha = 0f // Reset alpha for fade-in
                            },
                            text = {
                                Text(
                                    text = "${quiz.name}, ${quiz.topic}",
                                    color = if (selectedQuizIndex == index) Color.White else Color.Gray,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFF2196F3),
                                                    Color(0xFFE91E63)
                                                )
                                            ),
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display leaderboard for the selected quiz with fade-in effect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .alpha(fadeInAlpha) // Apply alpha for fade-in effect
                ) {
                    val selectedQuizId = quizzes[selectedQuizIndex].quizId
                    QuizLeaderboard(quizId = selectedQuizId, leaderboardViewModel = leaderboardViewModel)
                }
            } else {
                Text("Loading quizzes...", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}