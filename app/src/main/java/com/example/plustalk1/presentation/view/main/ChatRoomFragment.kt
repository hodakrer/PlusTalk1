package com.example.plustalk1.presentation.view.main

import ChatMessageAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plustalk1.R
import com.example.plustalk1.data.model.ChatMessage


class ChatRoomFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatMessageAdapter
    private val messageList = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_room, container, false)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerViewChat)
        chatAdapter = ChatMessageAdapter(messageList)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 더미 데이터 추가 (테스트용)
        messageList.add(ChatMessage("Hello!", false))
        messageList.add(ChatMessage("Hi, how are you?", true))
        messageList.add(ChatMessage("I'm good, thanks!", false))
        chatAdapter.notifyDataSetChanged()

        return view
    }


    private lateinit var editTextChat: EditText
    private lateinit var chatMessageAdapter: ChatMessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextChat = view.findViewById(R.id.editTextChat) // EditText 연결

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewChat)
        chatMessageAdapter = ChatMessageAdapter(messageList)
        recyclerView.adapter = chatMessageAdapter

        // 엔터키 눌렀을 때 메시지 처리
        editTextChat.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) { // 엔터키가 'SEND' 액션으로 처리될 때
                val messageText = editTextChat.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    // 유저의 메시지 생성
                    val message = ChatMessage(message = messageText, isSentByUser = true)
                    messageList.add(message)

                    // 어댑터 갱신
                    chatMessageAdapter.notifyItemInserted(messageList.size - 1)

                    // EditText 초기화
                    editTextChat.text.clear()

                    // 스크롤을 맨 아래로 이동
                    recyclerView.scrollToPosition(messageList.size - 1)
                }
                true
            } else {
                false
            }
        }

    }
}