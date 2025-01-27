package com.example.plustalk1.presentation.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plustalk1.R
import com.example.plustalk1.data.model.ChatAdapter
import com.example.plustalk1.data.model.ChatItem
import com.example.plustalk1.data.model.Friend
import com.example.plustalk1.data.model.FriendAdapter
import com.example.plustalk1.data.model.FriendListRequest
import com.example.plustalk1.data.model.OnRecyclerItemClickListener
import com.example.plustalk1.databinding.FragmentChatBinding



import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatFragment : Fragment(), OnRecyclerItemClickListener {

    private lateinit var searchEditText: EditText
    private var isSearchVisible = false // Track search bar visibility
    private var friendSearchQuery: String? = null
    private lateinit var binding: FragmentChatBinding
    //Change <Freind> To <ChatItem> When Server Is Ready
    private var datas: MutableList<Friend> = mutableListOf( )

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

        loadFriendList()

        return binding.root
    }

    override fun onRecyclerItemClick(email: String) {
        // Show a Toast message to confirm the item click
        Toast.makeText(requireContext(), "Clicked on email: $email", Toast.LENGTH_SHORT).show()

        // Create a Bundle to pass the email data
        val bundle = Bundle()
        bundle.putString("email", email)

        // Create an instance of ChatRoomFragment
        val chatRoomFragment = ChatRoomFragment()
        chatRoomFragment.arguments = bundle

        // Use FragmentTransaction to replace the current fragment with ChatRoomFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, chatRoomFragment)  // Replace with your container's ID
            .addToBackStack(null)  // Optionally add to back stack for navigating back
            .commit()
    }

    //Loading Current Friend's list
    private fun loadFriendList() {
        Log.e("kkang","Function loadFriendList called!")
        // Get stored email from SharedPreferences
        val email = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("email", null)

        MainActivity.RetrofitInstance.apiService.listFriend(FriendListRequest(email.toString()))
            .enqueue(object : Callback<List<Friend>> {
                override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                    if (response.isSuccessful) {
                        val friendList : List<Friend> = response.body() ?: emptyList()
                        datas.clear()
                        datas.addAll(friendList) // Add all friends to the datas list
                        binding.frChatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.frChatRecyclerView.adapter = ChatAdapter(datas, requireContext(), this@ChatFragment)
                        binding.frChatRecyclerView.adapter?.notifyDataSetChanged() // Update the RecyclerView
                    } else {
                        Log.e("FriendList", "Response error: ${response.code()}")
                        Toast.makeText(requireContext(), "Failed to load friends.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                    Log.e("FriendList", "Network error: ${t.localizedMessage}")
                    Toast.makeText(requireContext(), "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}