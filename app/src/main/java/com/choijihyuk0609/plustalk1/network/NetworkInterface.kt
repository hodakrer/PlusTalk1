package com.choijihyuk0609.plustalk1.network

import com.choijihyuk0609.plustalk1.data.model.ChatMessageCreateRequest
import com.choijihyuk0609.plustalk1.data.model.ChatMessageCreateResponse
import com.choijihyuk0609.plustalk1.data.model.ChatMessageListAllRequest
import com.choijihyuk0609.plustalk1.data.model.ChatMessageListAllResponse
import com.choijihyuk0609.plustalk1.data.model.ChatRoomCreateRequest
import com.choijihyuk0609.plustalk1.data.model.ChatRoomCreateResponse
import com.choijihyuk0609.plustalk1.data.model.ChatRoomListRequest
import com.choijihyuk0609.plustalk1.data.model.ChatRoomListResponse
import com.choijihyuk0609.plustalk1.data.model.FriendAddRequest
import com.choijihyuk0609.plustalk1.data.model.FriendAddResponse
import com.choijihyuk0609.plustalk1.data.model.FriendListRequest
import com.choijihyuk0609.plustalk1.data.model.FriendListResponse
import com.choijihyuk0609.plustalk1.data.model.FriendSearchRequest
import com.choijihyuk0609.plustalk1.data.model.FriendSearchResponse
import com.choijihyuk0609.plustalk1.data.model.SigninRequest
import com.choijihyuk0609.plustalk1.data.model.SigninResponse
import com.choijihyuk0609.plustalk1.data.model.SignupRequest
import com.choijihyuk0609.plustalk1.data.model.SignupResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkInterface {
    @POST("member/login")
    fun signin(
        @Body signinRequest: SigninRequest
    ): Call<SigninResponse>

    @POST("member/register")
    fun signup(
        @Body signupRequest: SignupRequest
    ): Call<SignupResponse>

    @POST("friend/search")
    fun searchFriend(
        @Body friendSearchRequest: FriendSearchRequest
    ): Call<FriendSearchResponse>

    @POST("friend/add")
    fun addFriend(
        @Body friendAddReqeust: FriendAddRequest
    ): Call<FriendAddResponse>


    @POST("friend/list")
    suspend fun listFriend(
        @Body friendListRequest: FriendListRequest
    ): FriendListResponse

    @POST("chatroom/create")
    fun createChatRoom(
        @Body chatRoomCreateRequest: ChatRoomCreateRequest
    ): Call<ChatRoomCreateResponse>

    @POST("chatroom/list")
    fun listChatroom(
        @Body chatRoomListRequest: ChatRoomListRequest
    ): Call<ChatRoomListResponse>

    @POST("chatmessage/list-all")
    fun listAllChatMessage(
        @Body chatMessageListAllRequest: ChatMessageListAllRequest
    ): Call<ChatMessageListAllResponse>

    @POST("chatmessage/create")
    fun createChatMessage(
        @Body chatMeessageCreateRequest: ChatMessageCreateRequest
    ): Call<ChatMessageCreateResponse>
}