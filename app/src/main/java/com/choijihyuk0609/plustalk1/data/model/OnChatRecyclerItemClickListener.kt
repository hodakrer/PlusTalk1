package com.choijihyuk0609.plustalk1.data.model

interface OnChatRecyclerItemClickListener {
    fun onRecyclerItemClick(email: String, friend: String, chatRoomId: String)
}