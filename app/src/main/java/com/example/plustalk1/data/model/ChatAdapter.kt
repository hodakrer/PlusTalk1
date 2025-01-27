package com.example.plustalk1.data.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plustalk1.databinding.ItemFriendBinding

class ChatAdapter(
    private val datas: List<Friend>,
    private val context: Context,
    private val listener: OnRecyclerItemClickListener
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    //Change when ChatItem is ready from server
    inner class ChatViewHolder(private val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition // Use bindingAdapterPosition instead of adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Pass the email of the clicked item to the listener
                    listener.onRecyclerItemClick(datas[position].email)
                }
            }
        }
        //Change into ChatItem when Server is Ready
        fun bind(chatItem: Friend) {
            binding.email.text = chatItem.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val friend = datas[position]

        holder.bind(friend) // Bind the Friend object to the ViewHolder
    }

    override fun getItemCount(): Int = datas.size
}