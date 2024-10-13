package com.example.quizapp.components

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

data class QuizQuestion(
    val id: Int,
    val question: String,
    val answers: Map<String, String?>,  // Answers map
    val correctAnswers: Map<String, Boolean>,  // Map of answer identifiers to Boolean values
    val category: String,
    val difficulty: String
)

data class QuizInfo(
    val name: String,
    val topic: String,
    val difficultyLevel: String, // Match this field with your DB
    val coverimage: String,
    val description: String
)
