package com.example.quizapp.model

data class QuizQuestion(
    val question_title: String = "",
    val options: Map<String, String?> = mapOf(), // For answer_a, answer_b, etc.
    val correctAnswers: Map<String, Boolean> = mapOf(), // For answer_a_correct, answer_b_correct, etc.
    val points: Int = 10,
    val quizId: String = ""
)

data class QuizInfo(
    val quizId: String = "",
    val name: String,
    val topic: String,
    val difficultyLevel: String, // Match this field with your DB
    val coverimage: String,
    val description: String
)
