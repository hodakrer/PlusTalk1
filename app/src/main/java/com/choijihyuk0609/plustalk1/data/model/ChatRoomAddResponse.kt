package com.choijihyuk0609.plustalk1.data.model

data class ChatRoomAddResponse(
    val status: Int,
    val message: String,
    val data: ChatRoomData?
)

data class ChatRoomData(
    val memberEmail: String?,
    val friendEmail: String?,
    val chatRoomId:  String?,
    val CreatedTime: String?
)


/*
* //예시
{
    "data": {
        "memberEmail": "qwe123@naver.com",
        "friendEmail": "qwe122@naver.com",
        "chatroomId": "qwe123_2025-01-30T11:42:57",
        "createdTime": "2025-01-30T11:42:57"
    },
    "message": "채팅방 생성 성공",
    "status": 200
}
* */