package com.example.quizapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val _quizQuestions = MutableLiveData<List<QuizQuestion>>()
    val quizQuestions: LiveData<List<QuizQuestion>> = _quizQuestions

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("questions")

    fun getQuizQuestions(quizId: String) {
        viewModelScope.launch {
            try {
                // Accessing the questions directly under the specified quizId node
                databaseReference.child(quizId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val questionList = mutableListOf<QuizQuestion>()
                            for (questionSnapshot in snapshot.children) {
                                val quizQuestion = questionSnapshot.getValue(QuizQuestion::class.java)
                                quizQuestion?.let { questionList.add(it) }
                            }
                            _quizQuestions.value = questionList
                            Log.d("QuizViewModel", "Loaded questions: $questionList") // Log loaded questions
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("QuizViewModel", "Database error: ${error.message}")
                            _quizQuestions.value = emptyList() // In case of error
                        }
                    })
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error fetching questions: ${e.message}")
                _quizQuestions.value = emptyList() // In case of error
            }
        }
    }
}