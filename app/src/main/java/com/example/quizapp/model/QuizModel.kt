package com.example.quizapp.model

data class QuizQuestion(
    val questionTitle: String,
    val options: Map<String, String>,  // Options like {"answer_a": "ls", "answer_b": "cp"}
    val correctAnswers: Map<String, Boolean>, // Correct answers like {"answer_a_correct": true, "answer_b_correct": false}
    val points: Int =0,
    val quizId: String = ""
)

data class QuizInfo(
    val name: String,
    val topic: String,
    val difficultyLevel: String, // Match this field with your DB
    val coverimage: String,
    val description: String
)