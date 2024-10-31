package com.example.quizapp.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.model.QuizInfo
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun QuizCard(quiz: QuizInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            GlideImage(imageModel = quiz.coverimage, modifier = Modifier.size(100.dp), contentDescription = quiz.name)
            Text(text = quiz.name, textAlign = TextAlign.Center,fontSize = 20.sp, fontWeight = FontWeight.Bold,)
            Text(text = quiz.topic, fontSize = 16.sp, textAlign = TextAlign.Center,)
            Text(text = "Difficulty: ${quiz.difficultyLevel}", fontSize = 16.sp, textAlign = TextAlign.Center,)
        }
    }
}
@Composable
fun QuizDetailDialog(quiz: QuizInfo,navController: NavController, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text(text = "Close") } },
        text = {
            Column {
                GlideImage(imageModel = quiz.coverimage, modifier = Modifier.size(100.dp).align(
                    Alignment.CenterHorizontally), contentDescription = quiz.name)
                Text(text = quiz.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Topic: ${quiz.topic}", fontSize = 18.sp)
                Text(text = "Difficulty: ${quiz.difficultyLevel}", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = quiz.description, fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    onClick = {
                        navController.navigate("quiz_screen/${quiz.quizId}")
                    }
                ) {
                    Text("Play Quiz", color = Color.White)
                }
            }
        }
    )
}