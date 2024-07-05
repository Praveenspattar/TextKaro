package com.example.chatapp.utils

import com.google.firebase.auth.FirebaseUser

sealed class MyAuthResult {
    data class Success(val user: FirebaseUser) : MyAuthResult()
    data class Error(val exception: Exception) : MyAuthResult()
}