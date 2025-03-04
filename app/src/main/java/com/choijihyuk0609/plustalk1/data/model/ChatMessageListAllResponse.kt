package com.choijihyuk0609.plustalk1.data.model
data class ChatMessageListAllResponse(
    val status: Int,
    val message: String,
    val data: List<ChatMessage>
)

