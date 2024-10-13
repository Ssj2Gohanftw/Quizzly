package com.example.quizapp.components
import com.google.firebase.database.FirebaseDatabase

fun writeQuizInfoToFirebase(quizInfo: QuizInfo) {
    val database = FirebaseDatabase.getInstance()
    val quizzesRef = database.getReference("Quizzes")  // Generate a unique ID for each quiz


    quizzesRef.push().setValue(quizInfo)
   }
