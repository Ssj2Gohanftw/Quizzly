package com.example.quizapp.model

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
                databaseReference.orderByChild("quizId").equalTo(quizId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val questionList = mutableListOf<QuizQuestion>()
                            for (questionSnapshot in snapshot.children) {
                                val quizQuestion = questionSnapshot.getValue(QuizQuestion::class.java)
                                quizQuestion?.let { questionList.add(it) }
                            }
                            _quizQuestions.value = questionList
                        }

                        override fun onCancelled(error: DatabaseError) {
                            _quizQuestions.value = emptyList() // In case of error
                        }
                    })
            } catch (e: Exception) {
                _quizQuestions.value = emptyList() // In case of error
            }
        }
    }
}
