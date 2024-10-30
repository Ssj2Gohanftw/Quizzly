package com.example.quizapp.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizQuestionCard(
    question: QuizQuestion,
    onOptionSelected: (selectedOption: String) -> Unit = {} // To handle option selection
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {

        // Display the question title
        Text(
            text = question.question_title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        // Display points if applicable
        if (question.points > 0) {
            Text(
                text = "Points: ${question.points}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Display the options for the question
        question.options.forEach { (key, value) ->
            if (value != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(key) } // Handle answer selection
                        .padding(8.dp)
                ) {
                    Text(text = "$key: $value", fontSize = 16.sp)
                }
            }
        }
    }
}
