package com.example.quizapp.pages

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.R
import com.example.quizapp.components.DifficultySelection
import com.example.quizapp.components.OptionInputFields
import com.example.quizapp.components.QuizInputField
import com.example.quizapp.model.AuthState
import com.example.quizapp.model.AuthViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

@Composable
fun TrQuizCreatePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context
) {
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("roleSelection")
            else -> Unit
        }
    }

    var subjectName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("Easy") }
    var topic by remember { mutableStateOf("") }
    var coverImageUri by remember { mutableStateOf<Uri?>(null) }
    var coverImageUrl by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("") }
    var questionList by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var isDetailsSubmitted by remember { mutableStateOf(false) }

    val database = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            coverImageUri = it
            val coverImageRef = storage.child("coverImages/${System.currentTimeMillis()}.jpg")
            coverImageRef.putFile(it).addOnSuccessListener {
                coverImageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    coverImageUrl = downloadUrl.toString()
                    Toast.makeText(context, "Cover image uploaded successfully!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { error ->
                    Toast.makeText(context, "Failed to get download URL: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { error ->
                Toast.makeText(context, "Failed to upload image: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .widthIn(min = 280.dp, max = 350.dp)
                .background(Color.DarkGray, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Fill the Details below and then start creating the quiz!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (!isDetailsSubmitted) {
                QuizInputField("Subject Name", subjectName) { subjectName = it }
                QuizInputField("Description", description) { description = it }
                QuizInputField("Topic", topic) { topic = it }

                // Label for difficulty selection
                Text(
                    text = "Choose Difficulty Level:",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )

                // Difficulty Level Options
                DifficultySelection(selectedDifficulty) { selectedDifficulty = it }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { pickImageLauncher.launch("image/*") }
                        .background(Color.LightGray, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Select Cover Image", color = Color.Gray)
                }

                Button(onClick = { isDetailsSubmitted = true }) {
                    Text("Start Creating Quiz")
                }
            } else {
                QuizInputField("Question", question) { question = it }

                OptionInputFields(
                    optionA, optionB, optionC, optionD,
                    onOptionAChange = { optionA = it },
                    onOptionBChange = { optionB = it },
                    onOptionCChange = { optionC = it },
                    onOptionDChange = { optionD = it }
                )

                QuizInputField("Correct Answer (A, B, C, D)", correctAnswer) { correctAnswer = it }

                Button(onClick = {
                    val newQuestion = mapOf(
                        "question_title" to question,
                        "options" to mapOf(
                            "answer_a" to optionA,
                            "answer_b" to optionB,
                            "answer_c" to optionC,
                            "answer_d" to optionD
                        ),
                        "correctAnswers" to mapOf(
                            "answer_a" to (correctAnswer == "A"),
                            "answer_b" to (correctAnswer == "B"),
                            "answer_c" to (correctAnswer == "C"),
                            "answer_d" to (correctAnswer == "D")
                        ),
                        "points" to 10
                    )
                    questionList = questionList + newQuestion

                    question = ""
                    optionA = ""
                    optionB = ""
                    optionC = ""
                    optionD = ""
                    correctAnswer = ""
                }) {
                    Text("Add Question")
                }
                Button(onClick = {
                    // Save quiz and questions to the database
                    database.child("Quizzes").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var highestId = 0
                            snapshot.children.forEach {
                                val key = it.key
                                if (key != null && key.startsWith("quizId")) {
                                    val id = key.removePrefix("quizId").toIntOrNull()
                                    if (id != null && id > highestId) {
                                        highestId = id
                                    }
                                }
                            }
                            val quizId = "quizId${highestId + 1}"

                            database.child("Subjects").addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(subjectSnapshot: DataSnapshot) {
                                    var highestSubjectId = 0
                                    subjectSnapshot.children.forEach {
                                        val key = it.key
                                        if (key != null && key.startsWith("subjectId")) {
                                            val id = key.removePrefix("subjectId").toIntOrNull()
                                            if (id != null && id > highestSubjectId) {
                                                highestSubjectId = id
                                            }
                                        }
                                    }
                                    val subjectId = "subjectId${highestSubjectId + 1}"

                                    val quizData = mapOf(
                                        "name" to subjectName,
                                        "description" to description,
                                        "difficultyLevel" to selectedDifficulty,
                                        "subjectId" to subjectId,
                                        "topic" to topic,
                                        "coverimage" to coverImageUrl
                                    )
                                    database.child("Quizzes").child(quizId).setValue(quizData)

                                    val subjectData = mapOf("subjectName" to subjectName)
                                    database.child("Subjects").child(subjectId).setValue(subjectData)

                                    questionList.forEachIndexed { index, question ->
                                        database.child("questions").child(quizId).child("question_id${index + 1}").setValue(question)
                                    }

                                    // Reset fields after creating quiz
                                    subjectName = ""
                                    description = ""
                                    selectedDifficulty = "Easy"
                                    topic = ""
                                    coverImageUrl = ""
                                    questionList = listOf()
                                    isDetailsSubmitted = false
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }) {
                    Text("Create Quiz")
                }
            }
        }
    }
}