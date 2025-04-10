package com.choijihyuk0609.plustalk1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choijihyuk0609.plustalk1.data.model.Friend
import com.choijihyuk0609.plustalk1.data.model.FriendListRequest
import com.choijihyuk0609.plustalk1.data.repository.RetrofitInstance
import kotlinx.coroutines.launch

class FriendViewModel : ViewModel() {
    // LiveData 또는 StateFlow로 데이터를 관리할 수 있습니다.
    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> get() = _friends

    fun loadFriends(_email: String?) {
        viewModelScope.launch {
            val email = _email

            try {
                // suspend 함수를 사용하여 네트워크 요청
                val response = RetrofitInstance.apiService.listFriend(FriendListRequest(email ?: ""))

                // 응답 데이터 처리
                _friends.value = response.data ?: emptyList()
            } catch (e: Exception) {
                Log.e("FriendViewModel", "Network error: ${e.localizedMessage}")
            }
        }
    }
}