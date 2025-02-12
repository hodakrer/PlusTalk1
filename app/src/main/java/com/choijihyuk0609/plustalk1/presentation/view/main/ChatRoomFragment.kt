package com.choijihyuk0609.plustalk1.presentation.view.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.ChatMessage
import com.choijihyuk0609.plustalk1.data.model.ChatMessageAdapter
import com.choijihyuk0609.plustalk1.data.model.ChatMessageCreateRequest
import com.choijihyuk0609.plustalk1.data.model.ChatMessageCreateResponse
import com.choijihyuk0609.plustalk1.data.model.ChatMessageListAllRequest
import com.choijihyuk0609.plustalk1.data.model.ChatMessageListAllResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatRoomFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatMessageAdapter
    private val messageList = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_room, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewChat)
        editTextChat = view.findViewById(R.id.editTextChat)

        // RecyclerView 초기화
        chatMessageAdapter = ChatMessageAdapter(messageList)
        recyclerView.adapter = chatMessageAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 채팅 메시지 로드
        loadChatMessages()

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
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val messageText = editTextChat.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    sendMessageToServer(messageText)
                    editTextChat.text.clear() // 입력 필드 초기화
                }
                true
            } else {
                false
            }
        }

    }

    private fun loadChatMessages() {
        val memberEmail = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("email", null) ?: return

        val chatRoomId = "your_chat_room_id_here" // 실제 채팅방 ID로 바꿔주세요

        val chatMessageListAllRequest = ChatMessageListAllRequest(memberEmail, chatRoomId)

        MainActivity.RetrofitInstance.apiService.listAllChatMessage(chatMessageListAllRequest)
            .enqueue(object : Callback<ChatMessageListAllResponse> {
                override fun onResponse(
                    call: Call<ChatMessageListAllResponse>,
                    response: Response<ChatMessageListAllResponse>
                ) {
                    if (response.isSuccessful) {
                        val chatMessagesData = response.body()?.data
                        if (chatMessagesData != null) {
                            messageList.clear()
                            messageList.addAll(chatMessagesData.map { data ->
                                ChatMessage(
                                    message = if (data.isImage == true) data.imageUrl else data.messageText, // 이미지 메시지 처리
                                    isSentByUser = data.senderEmail == memberEmail // 유저 본인 여부 판별
                                )
                            })
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
    }

    private fun sendMessageToServer(messageText: String) {
        val memberEmail = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("email", null) ?: return

        val chatRoomId = "your_chat_room_id_here" // 실제 채팅방 ID로 바꿔주세요

        val request = ChatMessageCreateRequest(
            senderEmail = memberEmail,
            chatRoomId = chatRoomId,
            messageId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(), // 임시 ID
            isImage = false,
            messageText = messageText
        )

        MainActivity.RetrofitInstance.apiService.createChatMessage(request)
            .enqueue(object : Callback<ChatMessageCreateResponse> {
                override fun onResponse(
                    call: Call<ChatMessageCreateResponse>,
                    response: Response<ChatMessageCreateResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        loadChatMessages() // 서버에서 메시지 목록 다시 불러오기
                    } else {
                        Toast.makeText(context, "메시지 전송 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ChatMessageCreateResponse>, t: Throwable) {
                    Toast.makeText(context, "네트워크 오류: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}