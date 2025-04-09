package com.choijihyuk0609.plustalk1.data.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.databinding.ItemChatBinding


class ChatAdapter(
    private val listener: OnChatRecyclerItemClickListener
) : ListAdapter<ChatRoom, ChatAdapter.ChatViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.chatRoomId == newItem.chatRoomId
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }

    inner class ChatViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val chat = getItem(position)
                    listener.onRecyclerItemClick(
                        chat.memberEmail ?: "",
                        chat.friendEmail ?: "",
                        chat.chatRoomId ?: ""
                    )
                }
            }
        }

        fun bind(chatRoom: ChatRoom) { // ChatRoom → ChatRoomListData
            binding.email.text = chatRoom.friendEmail ?: "" // friendEmail 표시
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))  // ListAdapter는 getItem() 사용
    }
}