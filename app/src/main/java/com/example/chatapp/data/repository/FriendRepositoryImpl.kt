package com.example.chatapp.data.repository

import android.util.Log
import com.example.chatapp.data.interfaces.FriendRepository
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.data.model.User
import com.google.firebase.firestore.FirebaseFirestore

class FriendRepositoryImpl(private val firestore: FirebaseFirestore) : FriendRepository {

    override fun getFriends(userId: String, onComplete: (List<UserModel>) -> Unit) {
        val userFriendsRef = firestore.collection("users").document(userId).collection("friends")

        userFriendsRef.get()
            .addOnSuccessListener { documents ->
                val friends = mutableListOf<UserModel>()
                Log.d("getFriends", "Number of friends documents retrieved: ${documents.size()}")
                for (document in documents) {
                    val friendId = document.id
                    firestore.collection("users").document(friendId).collection("profile").document("userProfile")
                        .get()
                        .addOnSuccessListener { friendDoc ->
                            val friend = friendDoc.toObject(User::class.java)
                            friend?.let {
                                friends.add(UserModel(it.profilePictureUrl, it.username, "Last message", friendId))
                                if (friends.size == documents.size()) {
                                    onComplete(friends)
                                }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("getFriends", it.message.toString())
                            onComplete(emptyList())
                        }
                }
            }
            .addOnFailureListener {
                Log.e("getFriends", it.message.toString())
                onComplete(emptyList())
            }
    }

}
