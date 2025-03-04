package com.choijihyuk0609.plustalk1.data.model

data class FriendListResponse(
    val status: Int,
    val message: String,
    val data: List<Friend>
)
