package com.choijihyuk0609.plustalk1.data.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.databinding.ItemChatBinding


class ChatAdapter(
    private val datas: List<ChatRoom>,
    private val context: Context,
    private val listener: OnChatRecyclerItemClickListener
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Log.d("kkang", "챗 아이템 클릭 시 멤버 이메일: ${datas[position].memberEmail}")
                    Log.d("kkang", "챗 아이템 클릭 시 프랜드 이메일: ${datas[position].friendEmail}")
                    Log.d("kkang", "챗 아이템 클릭 시 챗방 아이디: ${datas[position].chatRoomId}")
                    listener.onRecyclerItemClick(datas[position].memberEmail ?: "",
                                                datas[position].friendEmail ?: "",
                                                datas[position].chatRoomId ?: "")// 클릭된 채팅방의 이메일 전달
                    Log.d("kkang", "여기 ChatAdapter임. listener.onRecyclerItemClick 하고 나왔다는데요?")

                }
            }
        }

        fun bind(chatRoom: ChatRoom) { // ChatRoom → ChatRoomListData
            binding.email.text = chatRoom.friendEmail ?: "" // friendEmail 표시
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatRoom = datas[position]
        holder.bind(chatRoom) // 올바른 데이터 전달
    }

    override fun getItemCount(): Int = datas.size
}