package com.choijihyuk0609.plustalk1.data.model

data class ChatRoom (
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
