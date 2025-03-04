package com.choijihyuk0609.plustalk1.data.model

data class ChatMessage(
    val chatRoomId  : String,
    val messageTime : String,
    val messageId: Int,
    val senderEmail: String,
    val isImage: Boolean,
    val messageText: String,
    val imageUrl: String

)