package com.example.quizapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuizInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),

    )
}

@Composable
fun OptionInputFields(
    optionA: String,
    optionB: String,
    optionC: String,
    optionD: String,
    onOptionAChange: (String) -> Unit,
    onOptionBChange: (String) -> Unit,
    onOptionCChange: (String) -> Unit,
    onOptionDChange: (String) -> Unit
) {
    QuizInputField("Option A", optionA, onOptionAChange)
    QuizInputField("Option B", optionB, onOptionBChange)
    QuizInputField("Option C", optionC, onOptionCChange)
    QuizInputField("Option D", optionD, onOptionDChange)
}

@Composable
fun DifficultySelection(selectedDifficulty: String, onDifficultyChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf("Easy", "Medium", "Hard").forEach { difficulty ->
            val backgroundColor = when (difficulty) {
                "Easy" -> if (selectedDifficulty == "Easy") Color(0xFF4CAF50) else Color.Gray // Green for selected Easy
                "Medium" -> if (selectedDifficulty == "Medium") Color(0xFFFFEB3B) else Color.Gray // Yellow for selected Medium
                    "Hard" -> if (selectedDifficulty == "Hard") Color(0xFFF44336) else Color.Gray // Red for selected Hard
                else -> Color.LightGray
            }

            Box(
                modifier = Modifier
                    .background(
                        backgroundColor,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .clickable { onDifficultyChange(difficulty) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    difficulty,
                    color = if (selectedDifficulty == difficulty) Color.Black else Color.White
                )
            }
        }
    }
}