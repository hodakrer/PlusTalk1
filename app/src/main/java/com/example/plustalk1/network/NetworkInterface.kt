package com.example.plustalk1.network

import com.example.plustalk1.data.model.Friend
import com.example.plustalk1.data.model.FriendAddRequest
import com.example.plustalk1.data.model.FriendAddResponse
import com.example.plustalk1.data.model.FriendListRequest
import com.example.plustalk1.data.model.FriendSearchRequest
import com.example.plustalk1.data.model.FriendSearchResponse
import com.example.plustalk1.data.model.SigninRequest
import com.example.plustalk1.data.model.SigninResponse
import com.example.plustalk1.data.model.SignupRequest
import com.example.plustalk1.data.model.SignupResponse
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
    fun listFriend(
        @Body friendListRequest: FriendListRequest
    ): Call<List<Friend>>

}