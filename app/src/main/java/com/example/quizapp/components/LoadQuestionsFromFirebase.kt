package com.example.quizapp.components
import com.google.firebase.database.*

fun loadQuizQuestions(quizId: String, onQuestionsLoaded: (List<QuizQuestion>) -> Unit) {
    // Reference to Firebase Realtime Database
    val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Reference to the "questions" node
    val questionsRef = databaseRef.child("questions")

    // Query to get all questions for the specified quizId
    questionsRef.orderByChild("quizId").equalTo(quizId)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val questionsList = mutableListOf<QuizQuestion>()

                // Loop through the retrieved questions and add them to the list
                for (questionSnapshot in dataSnapshot.children) {
                    val question = questionSnapshot.getValue(QuizQuestion::class.java)
                    if (question != null) {
                        questionsList.add(question)
                    }
                }

                // Callback to return the list of questions
                onQuestionsLoaded(questionsList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors
                println("Error loading quiz questions: ${databaseError.message}")
            }
        })
}
