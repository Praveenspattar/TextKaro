package com.example.chatapp.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.AuthRepo
import com.example.chatapp.utils.MyAuthResult
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepo) :ViewModel() {

    private val _createAccountResult = MutableLiveData<MyAuthResult>()
    private val _signInResult = MutableLiveData<MyAuthResult>()

    val createAccountResult: LiveData<MyAuthResult> get() = _createAccountResult
    val signInResult: LiveData<MyAuthResult> get() = _signInResult

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            _createAccountResult.value = authRepo.createAccount(email, password)
        }
    }

    fun signIn(email: String,password: String) {
        viewModelScope.launch {
            _signInResult.value = authRepo.signIn(email, password)
        }
    }

}