package com.example.plustalk1.data.model

data class ChatMessage(
    val message: String,
    val isSentByUser: Boolean // 사용자가 보낸 메시지인지 여부
)