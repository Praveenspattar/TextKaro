package com.example.chatapp.data.interfaces

import com.example.chatapp.utils.MyAuthResult

interface AuthInterface {
    suspend fun createAccount(email: String, password: String) : MyAuthResult

    suspend fun signIn(email: String, password: String): MyAuthResult
}