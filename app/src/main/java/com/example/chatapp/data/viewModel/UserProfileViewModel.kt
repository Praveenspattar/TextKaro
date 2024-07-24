package com.example.chatapp.data.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.data.interfaces.UserRepository

class UserProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<Pair<Boolean, String?>>()
    val uploadStatus: LiveData<Pair<Boolean, String?>>
        get() = _uploadStatus

    fun uploadProfilePicture(userId: String, username: String, profilePictureUri: Uri, email: String) {
        userRepository.uploadProfilePicture(userId, username, profilePictureUri, email) { success, message ->
            _uploadStatus.postValue(Pair(success, message))
        }
    }
}