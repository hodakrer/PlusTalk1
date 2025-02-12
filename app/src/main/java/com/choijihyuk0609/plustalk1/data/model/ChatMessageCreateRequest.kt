package com.choijihyuk0609.plustalk1.data.model

// 새 메시지 요청을 위한 데이터 클래스
data class ChatMessageCreateRequest(
    val senderEmail: String,
    val chatRoomId: String,
    val messageId: Int,
    val isImage: Boolean,
    val messageText: String? = null
)