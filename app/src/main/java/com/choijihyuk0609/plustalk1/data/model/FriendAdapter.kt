package com.choijihyuk0609.plustalk1.data.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.databinding.ItemFriendBinding

import com.choijihyuk0609.plustalk1.network.NetworkInterface
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
            AddFriend(memberEmail, memberFriendEmail)
            //Execute REtrofit communcation for Adding the Friend


        }
    }

    fun AddFriend (memberEmail: String, memberFriendEmail: String){
        val request = FriendAddRequest(memberEmail, memberFriendEmail)
        RetrofitInstance.apiService.addFriend(request).enqueue(object : Callback<FriendAddResponse> {
            override fun onResponse(
                call: Call<FriendAddResponse>,
                response: Response<FriendAddResponse>
            ) {
                val check : Boolean? = response.body()?.querySuccession
                Log.d("kkang","Friend Added? ${check}")

            }

            override fun onFailure(call: Call<FriendAddResponse>, t: Throwable) {
                TODO("Not yet implemented")
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
