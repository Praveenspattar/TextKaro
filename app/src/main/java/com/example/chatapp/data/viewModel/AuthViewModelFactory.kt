package com.example.chatapp.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.data.repository.AuthRepo

class AuthViewModelFactory(private val authRepo: AuthRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepo) as T
    }
}