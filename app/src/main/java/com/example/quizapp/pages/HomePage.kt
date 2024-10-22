package com.example.quizapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.AuthState
import com.example.quizapp.AuthViewModel
import com.example.quizapp.components.QuizCard
import com.example.quizapp.components.QuizDetailDialog
import com.example.quizapp.model.QuizInfo
import com.example.quizapp.model.fetchQuizInfoFromFirebase
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomePage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    var quizList by remember { mutableStateOf(listOf<QuizInfo>()) }
    var selectedQuiz by remember { mutableStateOf<QuizInfo?>(null) }
    var filteredQuizList by remember { mutableStateOf(listOf<QuizInfo>()) }
    var searchQuery by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fetchQuizInfoFromFirebase { quizzes ->
            quizList = quizzes
            filteredQuizList=quizzes
        }
    }
    LaunchedEffect(searchQuery) {
        filteredQuizList = quizList.filter { quiz ->
            quiz.name.contains(searchQuery, ignoreCase = true) || quiz.topic.contains(searchQuery, ignoreCase = true)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search quizzes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true, trailingIcon =
            {  Icon(imageVector = Icons.Default.Search, contentDescription = "Search")}
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display quiz cards in a grid
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp), modifier = Modifier.padding(16.dp)) {
            items(filteredQuizList) { quiz ->
                QuizCard(quiz) {
                    selectedQuiz = quiz  // Set the selected quiz when card is clicked
                }
            }
        }

        // Show details in a dialog if a quiz is selected
        selectedQuiz?.let {
            QuizDetailDialog(
                navController = navController,
                quiz = it,
                onDismiss = { selectedQuiz = null }
            )
        }
    }
}
