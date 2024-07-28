package com.example.chatapp.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.data.interfaces.FriendRepository
import com.example.chatapp.data.model.UserModel

class FriendViewModel(private val friendRepository: FriendRepository) : ViewModel() {

    private val _friendsList = MutableLiveData<List<UserModel>>()
    val friendsList: LiveData<List<UserModel>> get() = _friendsList

    fun fetchFriends(userId: String) {
        friendRepository.getFriends(userId) { friends ->
            _friendsList.postValue(friends)
        }
    }
}
