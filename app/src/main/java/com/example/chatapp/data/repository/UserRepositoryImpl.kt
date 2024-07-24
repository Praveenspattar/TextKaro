package com.example.chatapp.data.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.chatapp.data.interfaces.UserRepository
import com.example.chatapp.ui.activities.MainActivity
import com.example.chatapp.utils.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserRepositoryImpl(private val context: Context) : UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    override fun uploadProfilePicture(userId: String, username: String, profilePictureUri: Uri, email: String, onComplete: (Boolean, String?) -> Unit) {
        val imageRef: StorageReference = storageRef.child("profile_images/${userId}.jpg")

        imageRef.putFile(profilePictureUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    saveUserInfo(userId, username, imageUrl, email) { success ->
                        onComplete(success, if (success) "Upload successful" else "Failed to upload user info")
                    }
                }.addOnFailureListener {
                    onComplete(false, it.message)
                }
            }
            .addOnFailureListener {
                onComplete(false, it.message)
            }
    }

    private fun saveUserInfo(userId: String, username: String, imageUrl: String, email: String, onComplete: (Boolean) -> Unit) {
        val user = User(userId, username, imageUrl, email)

        firestore.collection("users")
            .document(userId).collection("profile")
            .document("userProfile")
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(true)
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                (context as? Activity)?.finish()
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}