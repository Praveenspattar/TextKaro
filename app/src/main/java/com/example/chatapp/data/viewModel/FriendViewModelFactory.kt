package com.example.chatapp.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.data.interfaces.FriendRepository

class FriendViewModelFactory(private val friendRepository: FriendRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendViewModel(friendRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}