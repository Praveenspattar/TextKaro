package com.example.chatapp.data.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.chatapp.data.interfaces.UserRepository
import com.example.chatapp.ui.activities.MainActivity
import com.example.chatapp.data.model.User
import com.google.firebase.firestore.FieldValue
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

    override fun searchUserByEmail(email: String, onComplete: (User?) -> Unit) {
        firestore.collectionGroup("profile")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onComplete(null)
                } else {
                    for (document in documents) {
                        val user = document.toObject(User::class.java)
                        onComplete(user)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("mail",it.message.toString())
                onComplete(null)
            }
    }

    override fun addFriendAndCreateChat(currentUserId: String, friendUserId: String, onComplete: (Boolean) -> Unit) {
        addFriend(currentUserId, friendUserId) { success ->
            if (success) {
                createChat(currentUserId, friendUserId) { chatSuccess ->
                    onComplete(chatSuccess)
                }
            } else {
                onComplete(false)
            }
        }
    }

    private fun addFriend(currentUserId: String, friendUserId: String, onComplete: (Boolean) -> Unit) {
        val currentUserRef = firestore.collection("users").document(currentUserId).collection("friends").document(friendUserId)
        val friendUserRef = firestore.collection("users").document(friendUserId).collection("friends").document(currentUserId)

        firestore.runTransaction { transaction ->
            transaction.set(currentUserRef, mapOf("added" to true))
            transaction.set(friendUserRef, mapOf("added" to true))
        }.addOnSuccessListener {
            onComplete(true)
        }.addOnFailureListener {
            Log.e("addFriend", it.message.toString())
            onComplete(false)
        }
    }

    override fun deleteFriend(currentUserId: String, friendUserId: String, onComplete: (Boolean) -> Unit) {
        val currentUserRef = firestore.collection("users").document(currentUserId).collection("friends").document(friendUserId)
        val friendUserRef = firestore.collection("users").document(friendUserId).collection("friends").document(currentUserId)

        firestore.runTransaction { transaction ->
            transaction.delete(currentUserRef)
            transaction.delete(friendUserRef)

            // Delete the chat between the users
            val chatId = if (currentUserId < friendUserId) "$currentUserId-$friendUserId" else "$friendUserId-$currentUserId"
            val chatRef = firestore.collection("chats").document(chatId)
            transaction.delete(chatRef)
        }.addOnSuccessListener {
            onComplete(true)
        }.addOnFailureListener { exception ->
            Log.e("deleteFriend", exception.message.toString())
            onComplete(false)
        }
    }

    private fun createChat(currentUserId: String, friendUserId: String, onComplete: (Boolean) -> Unit) {
        val chatId = if (currentUserId < friendUserId) "$currentUserId-$friendUserId" else "$friendUserId-$currentUserId"
        val chatData = hashMapOf(
            "users" to listOf(currentUserId, friendUserId),
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("chats")
            .document(chatId)
            .set(chatData, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                Log.e("createChat", it.message.toString())
                onComplete(false)
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