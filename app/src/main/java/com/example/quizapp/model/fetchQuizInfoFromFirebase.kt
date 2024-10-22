package com.example.quizapp.model
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

fun fetchQuizInfoFromFirebase(onDataFetched: (List<QuizInfo>) -> Unit) {
    val database = FirebaseDatabase.getInstance().reference

    database.child("Quizzes").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val quizList = mutableListOf<QuizInfo>()

            for (quizSnapshot in snapshot.children) {
                val name = quizSnapshot.child("name").getValue(String::class.java) ?: ""
                val topic = quizSnapshot.child("topic").getValue(String::class.java) ?: ""
                val difficultyLevel = quizSnapshot.child("difficultyLevel").getValue(String::class.java) ?: ""
                val coverimage = quizSnapshot.child("coverimage").getValue(String::class.java) ?: ""
                val description = quizSnapshot.child("description").getValue(String::class.java) ?: ""

                // Create a QuizInfo object and add it to the list
                quizList.add(QuizInfo(name, topic, difficultyLevel, coverimage, description))
            }
            onDataFetched(quizList)  // Return the fetched quizzes
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle any errors
            println("Error fetching quizzes: ${error.message}")
        }
    })
}