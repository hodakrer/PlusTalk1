package com.choijihyuk0609.plustalk1.presentation.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.ChatAdapter
import com.choijihyuk0609.plustalk1.data.model.ChatRoom
import com.choijihyuk0609.plustalk1.data.model.OnChatRecyclerItemClickListener
import com.choijihyuk0609.plustalk1.databinding.FragmentChatBinding
import com.choijihyuk0609.plustalk1.presentation.viewmodel.ChatViewModel

class ChatFragment : Fragment(), OnChatRecyclerItemClickListener {
    private lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chat, container, false
        )

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        recyclerView = binding.frChatRecyclerView
        recyclerView.setHasFixedSize(true) // RecyclerView 크기 고정 (성능 최적화)

        // MainActivity에서 전달된 RecycledViewPool을 사용
        val pool = (activity as MainActivity).recycledViewPool
        recyclerView.setRecycledViewPool(pool)

        // ViewModel의 LiveData를 관찰
        viewModel.chatRoom.observe(viewLifecycleOwner, Observer { chatRoom ->
            if (chatRoom.isNotEmpty()) {
                // RecyclerView 갱신
                adapter.submitList(chatRoom) // ListAdapter의 submitList()로 리스트 갱신
            }
        })

        
        //어뎁터 초기화
        adapter = ChatAdapter(this@ChatFragment)
        recyclerView.adapter = adapter
        
        //어뎁터값을 레이아웃매니저로 넘김
        binding.frChatRecyclerView.layoutManager = LinearLayoutManager(requireContext( ) )

        binding.frChatRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext( ),
                LinearLayoutManager.VERTICAL)
        )
        val email = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("email", null)

        return binding.root
    }


    override fun onRecyclerItemClick(email: String, friend: String, chatRoomId: String) {
        Log.d("kkang", "onRecyclerItemClick and Email: ${email}")

        val bundle = Bundle()
        // 값이 null인 경우에는 Fragment를 생성하지 않도록 처리
        bundle.putString("member", email)
        bundle.putString("friend", friend)
        bundle.putString("chatroomid", chatRoomId)


        // 프래그먼트를 생성할 때 bundle을 인자로 넘김
        val chatRoomFragment = ChatRoomFragment.newInstance(email, friend, chatRoomId)

        // arguments를 설정하기 전에 fragment를 생성
        chatRoomFragment.arguments = bundle
        ////////////Log.d("kkang", "번들을 꺼내봅시다. member: ${bundle.getString("member") }")
        ////////////Log.d("kkang", "번들을 꺼내봅시다. friendName: ${bundle.getString("friend") }")
        ////////////Log.d("kkang", "번들을 꺼내봅시다. chatRoom: ${bundle.getString("chatroomid") }")
        // 프래그먼트 교체
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, chatRoomFragment)  // container의 ID로 교체
            .addToBackStack(null)  // 뒤로 가기 스택에 추가
            .commit()
        Log.d("kkang", "이거 나오면 chatFragment로 돌아온거임")
    }

    //Loading Current ChatRoom list
    /*
    private fun loadChatRoomList() {
        Log.e("kkang","Function loadFriendList called!")
        // Get stored email from SharedPreferences
        val email = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("email", null)
        val chatRoomRequest = ChatRoomListRequest(memberEmail = email)

        RetrofitInstance.apiService.listChatroom(chatRoomRequest)
            .enqueue(object : Callback<ChatRoomListResponse> {
                override fun onResponse(call: Call<ChatRoomListResponse>, response: Response<ChatRoomListResponse>) {

                    if (response.isSuccessful) {
                        val chatRoomList = response.body()?.data ?: emptyList()
                        //val chatRoomList = MutableList(10000) { index ->
                        //    ChatRoom(memberEmail = "$index", friendEmail = "$index", chatRoomId = "$index", createdTime = "")
                        //}

                        Log.d("kkang", "chatRoomList: $chatRoomList")

                        datas.clear()
                        datas.addAll(chatRoomList)

                        binding.frChatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.frChatRecyclerView.adapter = ChatAdapter(datas, requireContext(), this@ChatFragment)

                        binding.frChatRecyclerView.adapter?.notifyItemRangeInserted(0, chatRoomList.size)
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
    }*/


}