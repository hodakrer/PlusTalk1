package com.choijihyuk0609.plustalk1.data.model

data class FriendAddResponse(
    val status : Int,
    val message: String,
    val data: FriendAddDatas?
)

data class FriendAddDatas(
    val memberEmail: String,
    val friendMemberEmail: String,
    val friendSetTime: String
)