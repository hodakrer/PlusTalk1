package com.choijihyuk0609.plustalk1.data.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.repository.RetrofitInstance
import com.choijihyuk0609.plustalk1.databinding.ItemFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendAdapter(val datas: List<Friend>,
                    private val context: Context,
                    private val listener: OnRecyclerItemClickListener):
    ListAdapter<Friend, FriendAdapter.FriendViewHolder>(FriendDiffCallback()) {

    class FriendViewHolder(val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = getItem(position) // ListAdapter에서 제공하는 getItem
        holder.binding.email.text = friend.email
        holder.binding.itemRoot.setOnClickListener {
            listener.onRecyclerItemClick(friend.email)
            Log.d("kkang", "item root click : ${holder.binding.email.text}")

            // 친구 추가 기능
            val preferences: SharedPreferences = holder.itemView.context.getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val memberFriendEmail = holder.binding.email.text.toString()
            val memberEmail = preferences.getString("email", null).toString()
            Log.d("kkang", "emails prepared: ${memberEmail} and ${memberFriendEmail}")
            addFriend(memberEmail, memberFriendEmail)
        }
    }

    fun addFriend (memberEmail: String, memberFriendEmail: String){
        val request = FriendAddRequest(memberEmail, memberFriendEmail)
        RetrofitInstance.apiService.addFriend(request).enqueue(object : Callback<FriendAddResponse> {
            override fun onResponse(
                call: Call<FriendAddResponse>,
                response: Response<FriendAddResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("kkang", "Response: $body")
                    val status = body?.status
                    Log.d("kkang", "Friend Added? $status")

                    if (status == 200) {
                        Log.d("kkang", "Friend added successfully!")
                        createChatRoom(memberEmail, memberFriendEmail)
                    } else {
                        Log.d("kkang", "Failed to add friend. Status: $status, Message: ${body?.message}")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    if (response.code() == 409) {
                        createChatRoom(memberEmail, memberFriendEmail)
                        Log.d("kkang", "Error: Friend already exists. Message: $errorMessage")
                        // 사용자에게 이미 친구가 존재한다고 알리기
                        Toast.makeText(context, "이미 친구 목록에 있습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("kkang", "Error in response: $errorMessage")
                    }
                }
            }
            override fun onFailure(call: Call<FriendAddResponse>, t: Throwable) {
                Log.e("kkang", "Network error: ${t.message}")
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun createChatRoom(memberEmail: String, memberFriendEmail: String){
        val request = ChatRoomCreateRequest(memberEmail, memberFriendEmail)
        RetrofitInstance.apiService.createChatRoom(request).enqueue(object: Callback<ChatRoomCreateResponse> {
            override fun onResponse(
                call: Call<ChatRoomCreateResponse>,
                response: Response<ChatRoomCreateResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("kkang", "Response: $body")
                    val status = body?.status
                    Log.d("kkang", "Chatroom Created? $status")
                    val chatRoomId = body?.data?.chatRoomId

                    if ((body?.status == 200 && chatRoomId != null)) {
                        Log.d("kkang", "Chatroom created successfully!")
                        val sharedPref = context.getSharedPreferences(context.getString(R.string.CHATTINGROOM), Context.MODE_PRIVATE)
                        sharedPref.edit().putString(memberEmail, chatRoomId).apply()
                        Log.d("kkang", "response.code(): ${response.code( ) }")
                        Log.d("kkang", "chatRoomId: ${chatRoomId}")
                        Log.d("kkang","Checking SharedPref save: ${sharedPref.getString(memberEmail,"못가져왔는데용.") }" )


                    } else {
                        Log.d("kkang", "Failed to create Chatroom. Status: $status, Message: ${body?.message}")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    if (response.code() == 500) {
                        val sharedPref = context.getSharedPreferences(context.getString(R.string.CHATTINGROOM), Context.MODE_PRIVATE)
                        Log.d("kkang", "response.code(): ${response.code( ) }")
                        Log.d("kkang", "Checking SharedPref save: ${sharedPref.getString(memberEmail,"못가져왔는데용.")}")
                        Log.d("kkang", "Error: Chatroom already exists. Message: $errorMessage")
                        // 사용자에게 이미 친구가 존재한다고 알리기
                    } else {
                        Log.d("kkang", "Error in response: $errorMessage")
                    }
                    Log.d("kkang", "error code: ${response.code()}")
                }
                Log.d("kkang", "error code: ${response.code()}")

            }

            override fun onFailure(call: Call<ChatRoomCreateResponse>, t: Throwable) {
                Log.d("kkang", "createChatroom failed")


            }
        })
    }
}

