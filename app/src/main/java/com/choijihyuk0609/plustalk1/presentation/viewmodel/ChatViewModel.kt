package com.choijihyuk0609.plustalk1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choijihyuk0609.plustalk1.data.model.ChatRoom
import com.choijihyuk0609.plustalk1.data.model.ChatRoomListRequest
import com.choijihyuk0609.plustalk1.data.repository.RetrofitInstance
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel( ) {
    // LiveData 또는 StateFlow로 데이터를 관리할 수 있습니다.
    private val _chatRoom = MutableLiveData<List<ChatRoom>>()
    val chatRoom: LiveData<List<ChatRoom>> get() = _chatRoom

    fun loadChatRoomList(_email : String?) {
        viewModelScope.launch {
            val email = _email

            try {
                /////////// suspend 함수를 사용하여 네트워크 요청
                val response = RetrofitInstance.apiService.listChatroom(ChatRoomListRequest(email ?: ""))

                //////////// 응답 데이터 처리
                _chatRoom.value = response.data ?: emptyList()
            } catch (e: Exception) {
                Log.e("ChatFragmentViewModel", "Network error: ${e.localizedMessage}")
            }
        }
    }
}