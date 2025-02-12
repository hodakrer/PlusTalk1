package com.choijihyuk0609.plustalk1.data.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.databinding.ItemChatBinding


class ChatAdapter(
    private val datas: List<ChatRoomListData>,
    private val context: Context,
    private val listener: OnRecyclerItemClickListener
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onRecyclerItemClick(datas[position].memberEmail ?: "") // 클릭된 채팅방의 이메일 전달
                }
            }
        }

        fun bind(chatRoom: ChatRoomListData) { // ChatRoom → ChatRoomListData
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