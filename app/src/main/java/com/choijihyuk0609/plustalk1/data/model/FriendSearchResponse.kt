package com.choijihyuk0609.plustalk1.data.model

import com.google.gson.annotations.SerializedName

data class FriendSearchResponse(
    val status: Int,
    val message: String,
    val data: FriendSearchDatas
)

data class FriendSearchDatas(
    val email: String,
    val name: String,
    val profileImageUrl: String
)

