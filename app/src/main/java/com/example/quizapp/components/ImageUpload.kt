package com.example.quizapp.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

fun uploadImageToFirebase(
    imageUri: Uri,
    storageRef: StorageReference,
    databaseRef: DatabaseReference,
    context: Context,
    onUploadSuccess: (String) -> Unit
) {
    storageRef.putFile(imageUri)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val imageUrl = downloadUrl.toString()
                databaseRef.child("profilePicUrl").setValue(imageUrl)
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