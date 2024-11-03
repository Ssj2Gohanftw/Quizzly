package com.example.quizapp.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
//function to handle uploading of images to firebase storage as well as saving the url to the realtime database
fun uploadImageToFirebase(
    imageUri: Uri,
    storageRef: StorageReference,
    databaseRef: DatabaseReference,
    context: Context,
    onUploadSuccess: (String) -> Unit
) {
    // Upload image to Firebase Storage
    storageRef.putFile(imageUri)
        .addOnSuccessListener {
            // Get download URL
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                // Save download URL to Realtime Database
                val imageUrl = downloadUrl.toString()
                // Save the URL to the database
                databaseRef.child("profilePicUrl").setValue(imageUrl)
                    // Handle success or failure
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onUploadSuccess(imageUrl)
                        } else {
                            Toast.makeText(context, "Failed to save URL to database", Toast.LENGTH_SHORT).show()
                        }
                    }
            }.addOnFailureListener { downloadError ->
                Toast.makeText(context, "Failed to get download URL: ${downloadError.message}", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { uploadError ->
            Toast.makeText(context, "Failed to upload image: ${uploadError.message}", Toast.LENGTH_SHORT).show()
        }
}