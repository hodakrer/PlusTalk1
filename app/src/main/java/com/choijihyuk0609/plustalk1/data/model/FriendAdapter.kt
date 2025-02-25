package com.choijihyuk0609.plustalk1.data.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.databinding.ItemFriendBinding

import com.choijihyuk0609.plustalk1.network.NetworkInterface
import com.choijihyuk0609.plustalk1.presentation.view.main.AddFriendFragment
import com.choijihyuk0609.plustalk1.presentation.view.main.FriendFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FriendAdapter(val datas: List<Friend>,
                    private val context: Context,
                    private val listener: OnRecyclerItemClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>( ) {

    class FriendViewHolder(val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FriendViewHolder).binding
        binding.email.text = datas[position].email
        binding.itemRoot.setOnClickListener{
            listener.onRecyclerItemClick(datas[position].email )
            Log.d("kkang", "item root click : ${binding.email.text}")

            //Prepare Retrofit Communcation for Adding the Friend
            val preferences: SharedPreferences = context.getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val memberFriendEmail = binding.email.text.toString()
            val memberEmail = preferences.getString("email", null).toString()
            Log.d("kkang", "emails prepared: ${memberEmail} and ${memberFriendEmail}")
            addFriend(memberEmail, memberFriendEmail)
            //Execute REtrofit communcation for Adding the Friend

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
                } else {
                    Log.d("kkang", "Failed to add friend. Status: $status, Message: ${body?.message}")
                }
            } else {
                    val errorMessage = response.errorBody()?.string()
                    if (response.code() == 409) {
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
        }
        )

    }

    object RetrofitInstance {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder( )
                .baseUrl("https://rapapa.site/")
                .addConverterFactory(GsonConverterFactory.create( ) )
                .build( )
        }
        val apiService: NetworkInterface by lazy {
            retrofit.create(NetworkInterface::class.java)
        }
    }
    }
