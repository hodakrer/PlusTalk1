package com.choijihyuk0609.plustalk1.data.model

data class FriendAddResponse(
    var memberEmail      : String,
    var friendMemberEmail: String,
    var friendSetTime    : String,
    var querySuccession  : Boolean
)
