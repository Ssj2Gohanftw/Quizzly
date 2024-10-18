package com.example.quizapp
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) { // Ensure this class is public
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun saveUserRole(role: String) {
        sharedPreferences.edit().putString("user_role", role).apply()
    }

    fun getUserRole(): String? {
        return sharedPreferences.getString("user_role", null)
    }
}

