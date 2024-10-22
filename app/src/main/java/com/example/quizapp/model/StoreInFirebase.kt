package com.example.quizapp.model// Function to dynamically add questions to Firebase for a specific subject and quiz
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject

// Function to dynamically add questions to Firebase for a specific subject and quiz
fun addQuestionsToFirebase(
    subjectId: String,
    quizId: String,
    questionsJson: String // JSON data from a file (e.g., linux.json)
) {
    // Reference to Firebase Realtime Database
    val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Reference to the "questions" node
    val questionsRef = databaseRef.child("questions")

    // Parse the JSON string
    val jsonObj = JSONObject(questionsJson)
    val questionsArray = jsonObj.getJSONArray("questions")

    // Loop through the questions array and add each question to Firebase
    for (i in 0 until questionsArray.length()) {
        val questionObj = questionsArray.getJSONObject(i)

        // Generate a unique key for each question
        val questionKey = questionsRef.push().key

        if (questionKey != null) {
            // Create a QuizQuestion object from the JSON data
            val question = QuizQuestion(
                questionTitle = questionObj.getString("questionTitle"),
                options = mapOf(
                    "answer_a" to questionObj.getString("answer_a"),
                    "answer_b" to questionObj.getString("answer_b"),
                    "answer_c" to questionObj.getString("answer_c")
                ),
                correctAnswers = mapOf(
                    "answer_a_correct" to questionObj.getBoolean("answer_a_correct"),
                    "answer_b_correct" to questionObj.getBoolean("answer_b_correct"),
                    "answer_c_correct" to questionObj.getBoolean("answer_c_correct")
                ),
                points = questionObj.getInt("points"),
                quizId = quizId // Associate the question with the quizId
            )

            // Write the question data under the unique key in the "questions" node
            questionsRef.child(questionKey).setValue(question)
        }
    }
}

