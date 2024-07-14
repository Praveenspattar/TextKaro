package com.example.chatapp.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.chatapp.data.`interface`.AuthInterface
import com.example.chatapp.ui.activities.CreateAccount
import com.example.chatapp.ui.activities.MainActivity
import com.example.chatapp.utils.MyAuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(private val context: Context) : AuthInterface {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun createAccount(email: String, password: String): MyAuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                if (task.isSuccessful){
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }
            }.await()
            MyAuthResult.Success(result.user!!)
        } catch (e: Exception) {
            MyAuthResult.Error(e)
        }
    }

    override suspend fun signIn(email: String, password: String): MyAuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    }
                }.await()
            MyAuthResult.Success(result.user!!)
        } catch (e: Exception) {
            MyAuthResult.Error(e)
        }
    }
}