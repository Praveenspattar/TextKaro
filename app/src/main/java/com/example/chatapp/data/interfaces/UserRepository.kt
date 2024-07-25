package com.example.chatapp.data.interfaces

import android.net.Uri
import com.example.chatapp.utils.User

interface UserRepository {
    fun uploadProfilePicture(userId: String, username: String, profilePictureUri: Uri, email: String, onComplete: (Boolean, String?) -> Unit)

    fun searchUserByEmail(email: String, onComplete: (User?) -> Unit)
}