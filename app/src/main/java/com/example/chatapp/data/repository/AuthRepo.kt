package com.example.chatapp.data.repository

import com.example.chatapp.data.interfaces.AuthInterface
import com.example.chatapp.utils.MyAuthResult

class AuthRepo(private val fireServices : AuthInterface) {

    suspend fun createAccount(email: String, password: String): MyAuthResult {
        return fireServices.createAccount(email, password)
    }

    suspend fun signIn(email: String, password: String): MyAuthResult{
        return fireServices.signIn(email, password)
    }

}