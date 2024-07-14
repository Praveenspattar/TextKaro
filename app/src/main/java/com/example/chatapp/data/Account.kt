package com.example.chatapp.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.chatapp.data.repository.AuthRepo
import com.example.chatapp.data.viewModel.AuthViewModel
import com.example.chatapp.data.viewModel.AuthViewModelFactory
import com.example.chatapp.ui.activities.MainActivity
import com.example.chatapp.utils.MyAuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Account(val mContext: Context) {
//,private val viewModelStoreOwner: ViewModelStoreOwner

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    lateinit var viewModel: AuthViewModel

    fun getCurrentUser(): FirebaseUser? {
        return user
    }

    fun createAccount(email: String, password: String) {
        val authService = FirebaseAuthService(mContext) // need to implement this class
        val authRepository = AuthRepo(authService)
        viewModel = ViewModelProvider(mContext as ViewModelStoreOwner, AuthViewModelFactory(authRepository))[AuthViewModel::class.java]

        viewModel.createAccount(email, password)

        viewModel.createAccountResult.observe(mContext as LifecycleOwner, Observer { result ->
            when (result) {
                is MyAuthResult.Success -> {
                    // Account creation successful, handle accordingly
                    val user = result.user
                    Toast.makeText(mContext, "Account created successfully for ${user.email}", Toast.LENGTH_SHORT).show()
                    mContext.lifecycle
                }
                is MyAuthResult.Error -> {
                    // Account creation failed, handle accordingly
                    Toast.makeText(mContext, "Account creation failed: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun signIn(email: String, password: String){
        val authService = FirebaseAuthService(mContext) // need to implement this class
        val authRepository = AuthRepo(authService)
        viewModel = ViewModelProvider(mContext as ViewModelStoreOwner, AuthViewModelFactory(authRepository))[AuthViewModel::class.java]

        viewModel.signIn(email, password)

        viewModel.signInResult.observe(mContext as LifecycleOwner, Observer { result ->
            when (result) {
                is MyAuthResult.Success -> {
                    // Account creation successful, handle accordingly
                    val user = result.user
                    Toast.makeText(mContext, "Signed in as ${user.email}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(mContext,MainActivity::class.java)
                    mContext.startActivity(intent)
                }
                is MyAuthResult.Error -> {
                    // Account creation failed, handle accordingly
                    Toast.makeText(mContext, "Sign in failed: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
