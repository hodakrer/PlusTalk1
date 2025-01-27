package com.example.plustalk1.data.model

data class ChatItem (
    var email: String,
    var name: String,
    var profileImageUrl: String
) {
    init {
        email = ""
        name  = ""
        profileImageUrl = ""
    }
}
