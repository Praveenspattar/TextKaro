package com.example.chatapp.data.interfaces

import android.net.Uri
import com.example.chatapp.data.model.User

interface UserRepository {
    fun uploadProfilePicture(userId: String, username: String, profilePictureUri: Uri, email: String, onComplete: (Boolean, String?) -> Unit)

    fun searchUserByEmail(email: String, onComplete: (User?) -> Unit)

    fun addFriendAndCreateChat(currentUserId: String, friendUserId: String, onComplete: (Boolean) -> Unit)

    fun deleteFriend(currentUserId: String, friendUserId: String, onComplete: (Boolean) -> Unit)
}