package com.example.plustalk1.data.model

import com.google.gson.annotations.SerializedName

data class FriendSearchResponse(
    val email:  String,
    val name: String?,
    @SerializedName("profileImageUrl") val profileImageUrl: String?
)
