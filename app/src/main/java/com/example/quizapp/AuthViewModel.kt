package com.example.quizapp
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import android.content.Context

class AuthViewModel : ViewModel() {
    private val _auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    private val storageRef = FirebaseStorage.getInstance().reference
    private val databaseRef = FirebaseDatabase.getInstance().reference

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (_auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.UnAuthenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }
    fun trlogin(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }


    fun signup(name:String,email: String, password: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _authState.value = AuthState.Error("Name,Email and Password fields can't be empty!")
            return
        }
        _authState.value = AuthState.Loading
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated

                    // Store email and password in Realtime Database under a unique key in Students
                    val userId = _auth.currentUser?.uid
                    if (userId != null) {
                        val studentData = mapOf(
                            "name" to name,
                            "email" to email
                        )
                        // Generate a unique key under Students for each new signup
                        val newStudentRef = databaseRef.child("Students").push() // This generates a unique ID
                        newStudentRef.setValue(studentData)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    _authState.value = AuthState.Authenticated
                                } else {
                                    _authState.value = AuthState.Error(dbTask.exception?.message ?: "Failed to store user data")
                                }
                            }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun trsignup(name:String,email: String, password: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _authState.value = AuthState.Error("Name,Email and Password fields can't be empty!")
            return
        }
        _authState.value = AuthState.Loading
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated

                    // Store email and password in Realtime Database under a unique key in Students
                    val userId = _auth.currentUser?.uid
                    if (userId != null) {
                        val teacherData = mapOf(
                            "name" to name,
                            "email" to email
                        )
                        // Generate a unique key under Students for each new signup
                        val newTeacherRef = databaseRef.child("Teachers").push() // This generates a unique ID
                        newTeacherRef.setValue(teacherData)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    _authState.value = AuthState.Authenticated
                                } else {
                                    _authState.value = AuthState.Error(dbTask.exception?.message ?: "Failed to store user data")
                                }
                            }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signout() {
        _auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }

    fun getCurrentUser() = _auth.currentUser

    // Update Username
    fun uploadProfilePicture(context: Context, uri: Uri) {
        val userId = _auth.currentUser?.uid ?: return
        val profilePicRef = storageRef.child("profile_pictures/$userId.jpg")

        profilePicRef.putFile(uri)
            .addOnSuccessListener {
                profilePicRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveProfilePictureAndUsernameToDatabase(downloadUrl.toString(), userId)
                    updateFirebaseUserProfile(downloadUrl.toString())
                    Toast.makeText(context, "Profile picture updated", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error("Failed to upload profile picture: ${exception.message}")
            }
    }

    // Save both the image URL and the username to Firebase Realtime Database
    private fun saveProfilePictureAndUsernameToDatabase(imageUrl: String, userId: String) {
        val currentUser = getCurrentUser()
        val username = currentUser?.displayName ?: "Unknown User"

        val userUpdates = mapOf(
            "profilePicUrl" to imageUrl,
            "username" to username
        )

        databaseRef.child("Users").child(userId).updateChildren(userUpdates)
    }

    // Update Firebase Authentication profile with image URL
    private fun updateFirebaseUserProfile(imageUrl: String) {
        val user = _auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(imageUrl))
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "Error updating profile")
            }
        }
    }

    fun updateUsername(newUsername: String) {
        val user = _auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newUsername)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // After updating the Firebase Auth username, update it in the Realtime Database
                val userId = user.uid
                databaseRef.child("Users").child(userId).child("username").setValue(newUsername)
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "Error updating username")
            }
        }
    }

    fun updatePassword(newPassword: String) {
        val user = _auth.currentUser
        user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "Error updating password")
            }
        }
    }

    fun deleteAccount() {
        val user = _auth.currentUser
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.UnAuthenticated
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "Error deleting account")
            }
        }
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}