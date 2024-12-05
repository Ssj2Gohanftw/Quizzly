package com.example.quizapp.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

//Composable for the quiz cards which are used in the home page and make use of the QuizInfo data class
@Composable
fun QuizCard(quiz: QuizInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(5.dp)
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
//Composable for the quiz detail dialog which is shown when the quiz card is clicked
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
                Row(modifier = Modifier.padding(5.dp).height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    onClick = {
                        navController.navigate("quiz_screen/${quiz.quizId}")
                    }
                ) {
                    Text("Play Quiz", color = Color.White)
                }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { navController.navigate("leaderboards/${quiz.quizId}") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("View Leaderboard", color = Color.White)
                    }
                    }

            }
        }
    )
}
