package com.example.chatapp.data.model

data class ChatMessage(
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val messageText: String = "",
    val timestamp: Long = 0L,
    val messageType: String = "text" // "text", "image", etc.
)
