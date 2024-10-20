package com.example.quizapp.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val _quizQuestions = MutableLiveData<List<QuizQuestion>>()
    val quizQuestions: LiveData<List<QuizQuestion>> = _quizQuestions

    fun getQuizQuestions(apiKey: String, limit: Int, category: String, difficulty: String) {
        viewModelScope.launch {
            try {
                val response = QuizApiClient.api.getQuestions(
                    apiKey = apiKey,
                    limit = limit,
                    category = category,
                    difficulty = difficulty
                )
                _quizQuestions.value = response
            } catch (e: Exception) {
                _quizQuestions.value = emptyList() // In case of error
            }
        }
    }
}
