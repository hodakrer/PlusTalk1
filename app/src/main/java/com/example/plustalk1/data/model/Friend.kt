package com.example.plustalk1.data.model

data class Friend (
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