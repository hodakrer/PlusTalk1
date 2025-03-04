package com.choijihyuk0609.plustalk1.data.model

data class ChatRoomListResponse(
    val status: Int,
    val message: String,
    val data: List<ChatRoom>?
)
