package com.example.chatapp.data.interfaces

import com.example.chatapp.data.model.UserModel

interface FriendRepository {
    fun getFriends(userId: String, onComplete: (List<UserModel>) -> Unit)
}
