package com.example.quizapp.components
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun QuizQuestionCard(question: QuizQuestion) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = question.question,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(8.dp)
//        )
//
//        // Display choices for the question
//        question.answers.forEach { (key, value) ->
//            if (value != null) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { /* Handle answer selection */ }
//                        .padding(8.dp)
//                ) {
//                    Text(text = "$key: $value")
//                }
//            }
//        }
//    }
//}
