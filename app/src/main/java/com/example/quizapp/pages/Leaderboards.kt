package com.example.quizapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.components.LeaderboardCard
import com.example.quizapp.model.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    leaderboardViewModel: LeaderboardViewModel,
    quizId: String,
    userId: String
) {
    val playerDataList by leaderboardViewModel.leaderboardData.collectAsState()
    val userRank by leaderboardViewModel.userRank.collectAsState()

    LaunchedEffect(quizId) {
        leaderboardViewModel.fetchLeaderboard(quizId, userId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Add Leaderboard Heading
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Leaderboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(playerDataList) { playerData ->
            LeaderboardCard(playerData, isTopRank = playerData.rank == 1)
        }
    }
}

