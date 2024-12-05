package com.example.quizapp.model
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapp.components.LeaderboardCard
import com.google.firebase.auth.FirebaseAuth

@Composable
fun QuizLeaderboard(
    quizId: String,
    leaderboardViewModel: LeaderboardViewModel
) {
    val playerDataList by leaderboardViewModel.leaderboardData.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    LaunchedEffect(quizId) {
        leaderboardViewModel.fetchLeaderboard(quizId,userId)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(playerDataList) { playerData ->
            LeaderboardCard(playerData, playerData.rank == 1)
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(8.dp),color= Color.LightGray, thickness = 1.dp)
        }
    }
}