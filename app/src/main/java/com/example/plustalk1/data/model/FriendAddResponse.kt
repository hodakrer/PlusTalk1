package com.example.plustalk1.data.model

data class FriendAddResponse(
    var memberEmail      : String,
    var friendMemberEmail: String,
    var friendSetTime    : String,
    var querySuccession  : Boolean
)
