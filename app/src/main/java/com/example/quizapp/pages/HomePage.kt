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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.quizapp.components.Quiz
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomePage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value)
        {
            is AuthState.UnAuthenticated ->navController.navigate("login")
            else->Unit
            }
        }
val quizList= listOf(
    Quiz("DSA","Trees","Medium","https://placehold.co/400","Test your knowledge of different Trees like Binary Search Tree, AVL Tree, and Red-Black Tree")
    ,Quiz("Linux","Networking Commands","Easy","https://placehold.co/400","Want to Test your knowledge of Linux Networking Commands?This is the perfect quiz for you!")
    )

    var selectedQuiz by remember { mutableStateOf<Quiz?>(null)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally 
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text="Quizzes",
            fontSize=40.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
        LazyVerticalGrid(columns =GridCells.Adaptive(minSize = 150.dp),modifier=Modifier.padding(16.dp) )
        {
                items(quizList){
                    quiz->
                    QuizCard(quiz){
                        selectedQuiz=quiz
                    }
                }
        }
        selectedQuiz?.let {
            QuizDetailDialog(
                quiz = it,
                onDismiss = {selectedQuiz=null}
            )
      }
    }
}

@Composable
fun QuizCard(quiz:Quiz,onClick:()->Unit){
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
    )
    {
        Column(modifier = Modifier.padding(16.dp),horizontalAlignment = Alignment.CenterHorizontally)
        {
            GlideImage(imageModel =quiz.coverimage,modifier=Modifier.size(100.dp),contentDescription = quiz.name)
            Text(text=quiz.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text="Topic:${quiz.topic}", fontSize = 16.sp, textAlign = TextAlign.Center )
            Text(text="Difficulty:${quiz.difficulty}", fontSize = 16.sp,textAlign = TextAlign.Center)
        }
    }
}
@Composable
fun QuizDetailDialog(quiz: Quiz,onDismiss:()->Unit){
    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss){ Text(text = "Close") }},
        text={
            Column{
                Text(text="Topic:${quiz.topic}")
                Text(text="Difficulty: ${quiz.difficulty}")
                Text(text="Description: ${quiz.description}")
            }
        })
}