package com.example.quizapp.pages
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.R
import com.example.quizapp.model.AuthState
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.components.QuizCard
import com.example.quizapp.components.QuizDetailDialog
import com.example.quizapp.model.QuizInfo
import com.example.quizapp.model.fetchQuizInfoFromFirebase
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
@Composable
fun HomePage(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context
) {
    val isConnected = remember { mutableStateOf(isNetworkAvailable(context)) }

    // Monitor connectivity changes
    LaunchedEffect(Unit) {
        isConnected.value = isNetworkAvailable(context)
    }

    var quizList by remember { mutableStateOf(listOf<QuizInfo>()) }
    var selectedQuiz by remember { mutableStateOf<QuizInfo?>(null) }
    var filteredQuizList by remember { mutableStateOf(listOf<QuizInfo>()) }
    var searchQuery by remember { mutableStateOf("") }

    // Only fetch quizzes if connected
    if (isConnected.value) {
        LaunchedEffect(Unit) {
            fetchQuizInfoFromFirebase { quizzes ->
                quizList = quizzes
                filteredQuizList = quizzes
            }
        }
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

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
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
        } else {
            // Display a card indicating no quizzes are available due to no connection
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                    modifier = Modifier
                        .size(300.dp)
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                ){
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center)
                    {
                        Text(
                            text = "No quizzes available. Check your internet connection.",
                            color = Color.White, textAlign =  TextAlign.Center,
                            fontSize = 18.sp
                        )
                    }

                }
            }

        }
    }
}