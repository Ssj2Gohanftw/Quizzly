package com.example.quizapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.AuthState
import com.example.quizapp.AuthViewModel
import com.google.firebase.database.FirebaseDatabase

@Composable
fun TrQuizCreatePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    // Handle user authentication state
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("trlogin")
            else -> Unit
        }
    }

    // State variables for quiz input
    var quizTitle by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("") }

    // A list to store multiple questions for the quiz
    var questionList by remember { mutableStateOf(listOf<Map<String, String>>()) }

    // Firebase database instance
    val database = FirebaseDatabase.getInstance().reference

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            // Left column for quiz creation
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Create Quiz",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                BasicTextField(
                    value = quizTitle,
                    onValueChange = { quizTitle = it },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(8.dp),
                    decorationBox = { innerTextField -> if (quizTitle.isEmpty()) Text("Quiz Title") else innerTextField() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = question,
                    onValueChange = { question = it },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(8.dp),
                    decorationBox = { innerTextField -> if (question.isEmpty()) Text("Question") else innerTextField() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Options A-D
                BasicTextField(
                    value = optionA,
                    onValueChange = { optionA = it },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(8.dp),
                    decorationBox = { innerTextField -> if (optionA.isEmpty()) Text("Option A") else innerTextField() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = optionB,
                    onValueChange = { optionB = it },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(8.dp),
                    decorationBox = { innerTextField -> if (optionB.isEmpty()) Text("Option B") else innerTextField() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = optionC,
                    onValueChange = { optionC = it },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(8.dp),
                    decorationBox = { innerTextField -> if (optionC.isEmpty()) Text("Option C") else innerTextField() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = optionD,
                    onValueChange = { optionD = it },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(8.dp),
                    decorationBox = { innerTextField -> if (optionD.isEmpty()) Text("Option D") else innerTextField() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = correctAnswer,
                    onValueChange = { correctAnswer = it },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(8.dp),
                    decorationBox = { innerTextField -> if (correctAnswer.isEmpty()) Text("Correct Answer") else innerTextField() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button to add the current question to the list
                Button(onClick = {
                    val newQuestion = mapOf(
                        "question" to question,
                        "optionA" to optionA,
                        "optionB" to optionB,
                        "optionC" to optionC,
                        "optionD" to optionD,
                        "correctAnswer" to correctAnswer
                    )
                    questionList = questionList + newQuestion

                    // Clear input fields after adding the question
                    question = ""
                    optionA = ""
                    optionB = ""
                    optionC = ""
                    optionD = ""
                    correctAnswer = ""
                }) {
                    Text("Add Question")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit the full quiz to Firebase
                Button(onClick = {
                    val quizId = database.child("quizzes").push().key
                    val quizData = mapOf(
                        "title" to quizTitle,
                        "questions" to questionList
                    )
                    quizId?.let {
                        database.child("quizzes").child(it).setValue(quizData)
                    }
                }) {
                    Text("Submit Quiz")
                }
            }

            // Right column for displaying created questions
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                Text(
                    "Quiz Preview",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(questionList.size) { index ->
                        val questionItem = questionList[index]
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                "Q${index + 1}: ${questionItem["question"]}",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text("A: ${questionItem["optionA"]}")
                            Text("B: ${questionItem["optionB"]}")
                            Text("C: ${questionItem["optionC"]}")
                            Text("D: ${questionItem["optionD"]}")
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
