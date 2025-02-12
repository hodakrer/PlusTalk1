package com.choijihyuk0609.plustalk1.data.model

// 새 메시지 응답을 위한 데이터 클래스
data class ChatMessageCreateResponse(
    val status: Int,
    val message: String,
    val data: ChatMessageCreateData?
)

// 응답 내 실제 메시지 데이터
data class ChatMessageCreateData(
    val chatRoomId: String,
    val messageId: Int,
    val senderEmail: String,
    val messageTime: String,
    val messageText: String?,
    val imageUrl: String?,
    val isImage: Boolean
)