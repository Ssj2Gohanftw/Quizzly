package com.example.quizapp.model
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class LeaderboardViewModel : ViewModel() {

    private val leaderboardRef = FirebaseDatabase.getInstance().getReference("Leaderboard")
    private val studentsRef = FirebaseDatabase.getInstance().getReference("Students")
    private val quizzesRef = FirebaseDatabase.getInstance().getReference("Quizzes")
    private val _leaderboardData = MutableStateFlow<List<PlayerData>>(emptyList())
    val leaderboardData: StateFlow<List<PlayerData>> = _leaderboardData

    private val _userRank = MutableStateFlow<Int?>(null)
    val userRank: StateFlow<Int?> = _userRank

    private val _quizzes = MutableStateFlow<List<QuizInfo>>(emptyList())
    val quizzes: StateFlow<List<QuizInfo>> = _quizzes

    init {
        fetchQuizzes()
    }
    private fun fetchQuizzes() {
        quizzesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val quizList = mutableListOf<QuizInfo>()
                snapshot.children.forEach { quizSnapshot ->
                    val quizId = quizSnapshot.key ?: return@forEach
                    val name = quizSnapshot.child("name").getValue(String::class.java) ?: "Unknown Quiz"
                    val topic = quizSnapshot.child("topic").getValue(String::class.java) ?: "General"
                    val difficultyLevel = quizSnapshot.child("difficultyLevel").getValue(String::class.java) ?: "Medium"
                    val coverimage = quizSnapshot.child("coverimage").getValue(String::class.java) ?: ""
                    val description = quizSnapshot.child("description").getValue(String::class.java) ?: "No description available"

                    quizList.add(
                        QuizInfo(
                            quizId = quizId,
                            name = name,
                            topic = topic,
                            difficultyLevel = difficultyLevel,
                            coverimage = coverimage,
                            description = description
                        )
                    )
                }
                _quizzes.value = quizList
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }


    // Function to save player score with only userId and score in Leaderboard
    fun savePlayerScore(quizId: String, userId: String, score: Int) {
        leaderboardRef.child(quizId).child(userId).setValue(
            mapOf("userId" to userId, "score" to score)
        )
    }

    // Function to fetch leaderboard data and player details from Students
    fun fetchLeaderboard(quizId: String, userId: String) {
        viewModelScope.launch {
            leaderboardRef.child(quizId).orderByChild("score").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val playerList = mutableListOf<PlayerData>()
                    val studentIds = mutableListOf<String>()

                    snapshot.children.forEach { playerSnapshot ->
                        val playerUserId = playerSnapshot.key ?: return@forEach
                        val score = playerSnapshot.child("score").getValue(Int::class.java) ?: 0
                        studentIds.add(playerUserId)

                        playerList.add(PlayerData(userId = playerUserId, score = score))
                    }

                    // Fetch player details for each userId
                    studentsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(studentSnapshot: DataSnapshot) {
                            playerList.forEachIndexed { index, player ->
                                val studentData = studentSnapshot.child(player.userId)
                                val name = studentData.child("name").getValue(String::class.java) ?: "Unknown"
                                val profilePicUrl = studentData.child("profilePicUrl").getValue(String::class.java) ?: ""

                                playerList[index] = player.copy(name = name, profilePicUrl = profilePicUrl)
                            }

                            // Sort players by score and assign ranks
                            playerList.sortByDescending { it.score }
                            playerList.forEachIndexed { index, player ->
                                playerList[index] = player.copy(rank = index + 1)
                            }

                            _leaderboardData.value = playerList
                            _userRank.value = playerList.find { it.userId == userId }?.rank
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle database error
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
}