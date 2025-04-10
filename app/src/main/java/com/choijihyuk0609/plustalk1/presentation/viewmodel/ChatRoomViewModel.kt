package com.choijihyuk0609.plustalk1.presentation.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choijihyuk0609.plustalk1.data.model.ChatMessage
import com.choijihyuk0609.plustalk1.data.model.ChatMessageListAllRequest
import com.choijihyuk0609.plustalk1.data.model.ChatMessageListAllResponse
import com.choijihyuk0609.plustalk1.data.repository.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRoomViewModel(memberEmail: String, memberFriendEmail: String, chatRoomId: String) : ViewModel( ) {
    //memberEmail : String, memberFriendEmail : String, chatRoomId: String
    private val _chatMessages = MutableLiveData<List<ChatMessage>>()
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadChatMessages(memberEmail: String, chatRoomId: String) {
        val chatMessageListAllRequest = ChatMessageListAllRequest(memberEmail, chatRoomId)

        RetrofitInstance.apiService.listAllChatMessage(chatMessageListAllRequest)
            .enqueue(object : Callback<ChatMessageListAllResponse> {
                override fun onResponse(
                    call: Call<ChatMessageListAllResponse>,
                    response: Response<ChatMessageListAllResponse>
                ) {
                    if (response.isSuccessful) {
                        val chatMessagesData = response.body()?.data
                        _chatMessages.postValue(chatMessagesData ?: emptyList())
                    } else {
                        _errorMessage.postValue("서버 응답 실패")
                    }
                }

                override fun onFailure(call: Call<ChatMessageListAllResponse>, t: Throwable) {
                    _errorMessage.postValue("네트워크 오류: ${t.localizedMessage}")
                }
            })
    }
}