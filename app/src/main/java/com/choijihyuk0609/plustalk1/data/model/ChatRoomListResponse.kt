package com.choijihyuk0609.plustalk1.data.model

data class ChatRoomListResponse(
    val status:    Int,
    val message:   String,
    val data: List<ChatRoomListData>?
)

data class ChatRoomListData(
    val ChatRoomId:  String?,
    val memberEmail: String?,
    val friendEmail: String?,
    val createdTime: String?
)