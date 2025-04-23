package com.choijihyuk0609.plustalk1.presentation.view.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.ChatMessage
import com.choijihyuk0609.plustalk1.data.model.ChatMessageAdapter
import com.choijihyuk0609.plustalk1.data.model.ChatMessageCreateRequest
import com.choijihyuk0609.plustalk1.presentation.viewmodel.ChatRoomViewModel


class ChatRoomFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private val messageList = mutableListOf<ChatMessage>()
    private lateinit var editTextChat: EditText
    private var memberEmail: String? = null
    private var memberFriendEmail: String? = null
    private var chatRoomId: String? = null
    private lateinit var viewModel: ChatRoomViewModel


    companion object {
        fun newInstance(email: String, friend: String, chatroomid: String): ChatRoomFragment {
            val fragment = ChatRoomFragment()
            val args = Bundle()
            args.putString("member", email)
            args.putString("friend", friend)
            args.putString("chatroomid", chatroomid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // arguments에서 값 가져오기 (올바른 방식)
        arguments?.let {
            memberEmail = it.getString("member")
            memberFriendEmail = it.getString("friend")
            chatRoomId = it.getString("chatroomid")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_room, container, false)

        // ViewModel을 Fragment에서 가져옴
        viewModel = ViewModelProvider(this).get(ChatRoomViewModel() :: class.java)

        //UI 뷰바인딩
        recyclerView = view.findViewById(R.id.recyclerViewChat)
        editTextChat = view.findViewById(R.id.editTextChat)

        //어뎁터, 리사이클러뷰 설정
        memberEmail?.let { email ->
            chatMessageAdapter = ChatMessageAdapter(messageList, email)
            recyclerView.adapter = chatMessageAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        } ?: run {
            Log.e("ChatRoomFragment", "memberEmail is null")
        }
        //뷰모델을 라이브데이터로 관찰

        // 채팅 메시지 로드
        //loadChatMessages(memberEmail!!, memberFriendEmail!!, chatRoomId!!)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // EditText 연결
        editTextChat = view.findViewById(R.id.editTextChat)

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewChat)

        // 어댑터 초기화 (초기엔 빈 리스트로 시작)
        chatMessageAdapter = ChatMessageAdapter(mutableListOf(), memberEmail!!)
        recyclerView.adapter = chatMessageAdapter

        // ViewModel 초기화 (ViewModelProvider를 사용해서 가져오세요)
        val viewModel = ViewModelProvider(this)[ChatRoomViewModel::class.java]

        // LiveData observe: 메시지 변화 감지 → 어댑터 업데이트
        viewModel.chatMessages.observe(viewLifecycleOwner) { newMessages ->
            // 어댑터 안에 messageList를 직접 바꾸는 방식이라면:
            messageList.clear()
            messageList.addAll(newMessages)
            chatMessageAdapter.notifyDataSetChanged()

            // 또는 만약 submitList 지원 어댑터라면:
            // chatMessageAdapter.submitList(newMessages)
        }

        // 메시지 로드 호출
        viewModel.loadChatMessages(memberEmail!!, chatRoomId!!)

        // 엔터키 눌렀을 때 메시지 처리
        editTextChat.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val messageText = editTextChat.text.toString().trim()
                if (messageText.isNotEmpty()) {

                    Log.d("kkang", "messageText: ${messageText}")
                    Log.d("kkang", "chatRoom Info")
                    Log.d("kkang", "memberEmail: ${memberEmail}")
                    Log.d("kkang", "memberFriendEmail: ${memberFriendEmail}")
                    Log.d("kkang", "chatRoomId: ${chatRoomId}")

                    sendMessageToServer(messageText)
                    viewModel.loadChatMessages(memberEmail!!, chatRoomId!!)
                    editTextChat.text.clear() // 입력 필드 초기화
                }
                true
            } else {
                false
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            viewModel.loadChatMessages(memberEmail!!, chatRoomId!!)
            handler.postDelayed(this, 1000) // 1초마다 실행
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadChatMessages(memberEmail!!, chatRoomId!!) // 화면에 나타나면 바로 실행
        handler.post(refreshRunnable) // 주기적 실행 시작
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshRunnable) // 화면에서 사라지면 중단
    }

    private fun sendMessageToServer(messageText: String) {

        val sharedPref = requireContext().getSharedPreferences(requireContext().getString(R.string.CHATTINGROOM), Context.MODE_PRIVATE)
        val chatRoomId = sharedPref.getString(memberEmail, "none")

        val request = ChatMessageCreateRequest(
            senderEmail = memberEmail!!,
            chatRoomId = chatRoomId!!,
            messageId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(), // 임시 ID
            isImage = false,
            messageText = messageText
        )
    }

    /*
    private fun loadChatMessages(memberEmail : String, memberFriendEmail : String, chatRoomId: String) {

        if (memberEmail != null && chatRoomId != null){
            val chatMessageListAllRequest = ChatMessageListAllRequest(memberEmail, chatRoomId)

            RetrofitInstance.apiService.listAllChatMessage(chatMessageListAllRequest)
                .enqueue(object : Callback<ChatMessageListAllResponse> {
                    override fun onResponse(
                        call: Call<ChatMessageListAllResponse>,
                        response: Response<ChatMessageListAllResponse>
                    ) {
                        if (response.isSuccessful) {
                            val chatMessagesData = response.body()?.data
                            Log.d("kkang", "ChatMessagesData: ${chatMessagesData}")
                            if (chatMessagesData != null) {
                                messageList.clear()
                                messageList.addAll(chatMessagesData)
                                chatMessageAdapter.notifyDataSetChanged()
                            }
                        } else {
                            Toast.makeText(context, "Failed to load messages", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ChatMessageListAllResponse>, t: Throwable) {
                        Toast.makeText(context, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Log.d("kkang", "memberEmail: ${memberEmail} \n chatRoomId: ${chatRoomId}")
        }

    }*/

}