package com.example.chatapp.data.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.data.interfaces.UserRepository
import com.example.chatapp.data.model.User

class UserProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<Pair<Boolean, String?>>()
    val uploadStatus: LiveData<Pair<Boolean, String?>>
        get() = _uploadStatus

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _operationResult = MutableLiveData<Boolean>()
    val operationResult: LiveData<Boolean> get() = _operationResult

    private val _deletionResult = MutableLiveData<Boolean>()
    val deletionResult: LiveData<Boolean> get() = _deletionResult

    fun uploadProfilePicture(userId: String, username: String, profilePictureUri: Uri, email: String) {
        userRepository.uploadProfilePicture(userId, username, profilePictureUri, email) { success, message ->
            _uploadStatus.postValue(Pair(success, message))
        }
    }

    fun searchUserByEmail(email: String) {
        userRepository.searchUserByEmail(email) {user ->
            _user.postValue(user)
        }
    }

    fun addFriendAndCreateChat(currentUserId: String, friendUserId: String) {
        userRepository.addFriendAndCreateChat(currentUserId, friendUserId) { success ->
            _operationResult.postValue(success)
        }
    }

    fun deleteFriend(currentUserId: String, friendUserId: String) {
        userRepository.deleteFriend(currentUserId, friendUserId) { success ->
            _deletionResult.postValue(success)
        }
    }
}