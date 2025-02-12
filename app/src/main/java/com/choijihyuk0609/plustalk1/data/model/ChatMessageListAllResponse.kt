package com.choijihyuk0609.plustalk1.data.model
data class ChatMessageListAllResponse(
    val status: Int,
    val message: String,
    val data: List<ChatMessageListAllData>
)

data class ChatMessageListAllData(
    val chatRoodId  : String,
    val messageTime : String,
    val messageId: Int,
    val senderEmail: String,
    val isImage: Boolean,
    val messageText: String,
    val imageUrl: String
)
