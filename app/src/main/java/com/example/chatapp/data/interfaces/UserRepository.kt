package com.example.chatapp.data.interfaces

import android.net.Uri

interface UserRepository {
    fun uploadProfilePicture(userId: String, username: String, profilePictureUri: Uri, email: String, onComplete: (Boolean, String?) -> Unit)
}