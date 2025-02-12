package com.choijihyuk0609.plustalk1.presentation.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.ChatAdapter
import com.choijihyuk0609.plustalk1.data.model.ChatRoomListData
import com.choijihyuk0609.plustalk1.data.model.ChatRoomListRequest
import com.choijihyuk0609.plustalk1.data.model.ChatRoomListResponse
import com.choijihyuk0609.plustalk1.data.model.OnRecyclerItemClickListener
import com.choijihyuk0609.plustalk1.databinding.FragmentChatBinding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatFragment : Fragment(), OnRecyclerItemClickListener {


    private lateinit var binding: FragmentChatBinding
    private var datas: MutableList<ChatRoomListData> = mutableListOf( )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chat, container, false
        )

        binding.frChatRecyclerView.layoutManager = LinearLayoutManager(requireContext( ) )
        binding.frChatRecyclerView.adapter = ChatAdapter(datas, requireContext(), this@ChatFragment)
        binding.frChatRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext( ),
                LinearLayoutManager.VERTICAL)
        )

        loadChatRoomList()

        return binding.root
    }

    override fun onRecyclerItemClick(email: String) {
        // Show a Toast message to confirm the item click
        //Toast.makeText(requireContext(), "Clicked on email: $email", Toast.LENGTH_SHORT).show()

        // Create a Bundle to pass the email data
        val bundle = Bundle()
        bundle.putString("email", email)

        // Create an instance of ChatRoomFragment
        val chatRoomFragment = ChatRoomFragment()
        chatRoomFragment.arguments = bundle

        //Communicate with server -> making chatroom
        // Use FragmentTransaction to replace the current fragment with ChatRoomFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, chatRoomFragment)  // Replace with your container's ID
            .addToBackStack(null)  // Optionally add to back stack for navigating back
            .commit()
    }

    //Loading Current ChatRoom list
    private fun loadChatRoomList() {
        Log.e("kkang","Function loadFriendList called!")
        // Get stored email from SharedPreferences
        val email = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("email", null)
        val chatRoomRequest = ChatRoomListRequest(memberEmail = email)

        MainActivity.RetrofitInstance.apiService.listChatroom(chatRoomRequest)
            .enqueue(object : Callback<ChatRoomListResponse> {
                override fun onResponse(call: Call<ChatRoomListResponse>, response: Response<ChatRoomListResponse>) {

                    if (response.isSuccessful) {
                        val chatRoomList = response.body()?.data ?: emptyList()

                        datas.clear()
                        datas.addAll(chatRoomList) // ChatRoomListData 타입을 추가

                        binding.frChatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.frChatRecyclerView.adapter = ChatAdapter(datas, requireContext(), this@ChatFragment)
                        binding.frChatRecyclerView.adapter?.notifyDataSetChanged()
                    } else {
                        Log.e("ChatRoomList", "Response error: ${response.code()}")
                        Toast.makeText(requireContext(), "Failed to load chat rooms.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ChatRoomListResponse>, t: Throwable) {
                    Log.e("FriendList", "Network error: ${t.localizedMessage}")
                    Toast.makeText(requireContext(), "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}