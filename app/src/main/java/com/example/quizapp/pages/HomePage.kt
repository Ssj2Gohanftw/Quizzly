package com.example.quizapp.pages
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.R
import com.example.quizapp.components.LoadingAnimation
import com.example.quizapp.components.NoInternet
import com.example.quizapp.components.QuizCard
import com.example.quizapp.components.QuizDetailDialog
import com.example.quizapp.components.isNetworkAvailable
import com.example.quizapp.model.AuthState
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.model.QuizInfo
import com.example.quizapp.model.fetchQuizInfoFromFirebase

//Composable for home page, which shows a list of quizzes
@Composable
fun HomePage(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val isConnected = remember { mutableStateOf(isNetworkAvailable(context)) }// Monitor network connectivity
    val authState = authViewModel.authState.observeAsState()// Observe authentication state
    // Monitor connectivity changes
    LaunchedEffect(Unit) {
        isConnected.value = isNetworkAvailable(context)
    }
    // Navigate to role selection page if the user is unauthenticated
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("roleSelection")
            else -> Unit
        }
    }
    // State variables for quizzes and search functionality
    var quizList by remember { mutableStateOf(listOf<QuizInfo>()) }
    var selectedQuiz by remember { mutableStateOf<QuizInfo?>(null) }
    var filteredQuizList by remember { mutableStateOf(listOf<QuizInfo>()) }
    var searchQuery by remember { mutableStateOf("") }

    // Only fetch quizzes if connected
    if (isConnected.value) {
        LaunchedEffect(Unit) {
            try {
                fetchQuizInfoFromFirebase { quizzes ->
                    quizList = quizzes
                    filteredQuizList = quizzes
                    isLoading = false
                }
            }
            catch (e: Exception) {
                isError = true
                isLoading = false
            }
        }
        // Filter quizzes based on search query
        LaunchedEffect(searchQuery) {
            filteredQuizList = quizList.filter { quiz ->
                quiz.name.contains(searchQuery, ignoreCase = true) || quiz.topic.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    // Display quizzes based on connectivity
        if (isConnected.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
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
                    singleLine = true,
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (isLoading) {
                    LoadingAnimation()
                } else if (isError) {
                    Toast.makeText(
                        context,
                        "Error fetching quizzes,please try again later!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 110.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        items(filteredQuizList) { quiz ->
                            QuizCard(quiz) {
                                selectedQuiz = quiz
                            }
                        }
                    }
                    selectedQuiz?.let {
                        QuizDetailDialog(
                            navController = navController,
                            quiz = it,
                            onDismiss = { selectedQuiz = null }
                        )
                    }
                }
            }
        }
            else {
            // Display a card indicating no quizzes are available due to no connection
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoInternet()
            }

        }
    }
}